package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.exception.ChatExistsException;
import ru.itmo.hungergames.model.entity.chat.Chat;
import ru.itmo.hungergames.model.entity.chat.Message;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.ChatCreateRequest;
import ru.itmo.hungergames.model.request.MessageRequest;
import ru.itmo.hungergames.model.response.ChatResponse;
import ru.itmo.hungergames.model.response.MessageResponse;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.util.SecurityUtil;
import ru.itmo.hungergames.utils.MockAuth;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
class ChatServiceImplTest {
    @Autowired
    ChatServiceImpl chatService;

    @MockBean
    SecurityUtil securityUtil;
    @MockBean
    TributeRepository tributeRepository;
    @MockBean
    SponsorRepository sponsorRepository;
    @MockBean
    ChatRepository chatRepository;
    @MockBean
    MessageRepository messageRepository;

    @Autowired
    private MockAuth mockAuth;

    private final Mentor mentor = Mentor.builder()
            .id(new UUID(0, 2))
            .name("mentor")
            .userRoles(Set.of(UserRole.MENTOR))
            .build();
    private final Tribute tribute = Tribute.builder()
            .id(new UUID(0, 0))
            .name("tribute")
            .userRoles(Set.of(UserRole.TRIBUTE))
            .mentor(mentor)
            .build();
    private final Sponsor sponsor = Sponsor.builder()
            .id(new UUID(0, 1))
            .name("sponsor")
            .userRoles(Set.of(UserRole.SPONSOR))
            .build();
    private final Chat chat = Chat.builder()
            .id(new UUID(42, 42))
            .tribute(tribute)
            .sponsor(sponsor)
            .messages(List.of())
            .build();

    @BeforeEach
    void setUp() {
        Mockito.doReturn(Optional.of(tribute))
                .when(tributeRepository)
                .findById(tribute.getId());

        Mockito.doReturn(Optional.of(sponsor))
                .when(sponsorRepository)
                .findById(sponsor.getId());

        this.mockAuth.authenticate(sponsor);
    }

    private boolean createdChatCorrect(Chat actualChat) {
        return (actualChat.getMessages() == null || actualChat.getMessages().isEmpty()) &&
                actualChat.getTribute().equals(this.tribute) &&
                actualChat.getSponsor().equals(this.sponsor);
    }

    @Test
    void createChat() {
        doReturn(this.chat).when(chatRepository)
                .save(argThat(actualChat -> createdChatCorrect(actualChat)));

        final ChatResponse chatResponse = chatService.createChat(new ChatCreateRequest(this.tribute.getId()));

        Mockito.verify(chatRepository, Mockito.times(1))
                .save(argThat(actualChat -> createdChatCorrect(actualChat)));

        Assertions.assertEquals(new ChatResponse(this.chat), chatResponse);
    }

    @Test
    void validateBeforeCreateFailure() {
        doReturn(true)
                .when(chatRepository)
                .existsAllByTribute_IdAndSponsor_id(this.tribute.getId(), this.sponsor.getId());

        Throwable thrown = catchThrowable(() -> chatService.createChat(new ChatCreateRequest(this.tribute.getId())));

        assertThat(thrown).isInstanceOf(ChatExistsException.class);
        assertThat(thrown.getMessage()).contains(
                String.format("Chat for tribute with id=%s and sponsor with id=%s already exists.",
                        this.tribute.getId(), this.sponsor.getId()));
    }

    private boolean messageCorrectNoTime(String message, Message actualMessage) {
        return actualMessage.getChat().equals(this.chat) &&
                actualMessage.getUser().equals(this.sponsor) &&
                actualMessage.getMessage().equals(message);
    }

    private boolean messageCorrect(String message, Instant start, Instant end, Message actualMessage) {
        return this.messageCorrectNoTime(message, actualMessage) &&
                actualMessage.getDateTime().isAfter(start) &&
                actualMessage.getDateTime().isBefore(end);
    }

    @Test
    void sendMessage() {
        Mockito.doReturn(Optional.of(this.chat))
                .when(chatRepository)
                .findById(this.chat.getId());

        String textMessage = "Some message text";

        final var returnMessage = Message.builder()
                .id(new UUID(42, 42))
                .dateTime(Instant.now())
                .chat(this.chat)
                .message(textMessage)
                .user(this.sponsor)
                .build();

        doReturn(returnMessage).when(this.messageRepository)
                .save(argThat(message -> this.messageCorrectNoTime(textMessage, message)));

        final var start = Instant.now();

        final MessageResponse messageResponse = chatService.sendMessage(this.chat.getId(),
                new MessageRequest(textMessage));

        final var end = Instant.now();

        Mockito.verify(messageRepository, Mockito.times(1)).save(
                argThat(message -> this.messageCorrect(textMessage, start, end, message)));

        Assertions.assertEquals(new MessageResponse(returnMessage), messageResponse);
    }

    @Test
    void getMessagesByChatId() {
        final var message1 = Message.builder()
                .id(new UUID(0, 0))
                .chat(this.chat)
                .message("message1")
                .user(mentor)
                .build();

        final var message2 = Message.builder()
                .id(new UUID(0, 1))
                .chat(this.chat)
                .message("message2")
                .user(tribute)
                .build();

        final var messages = List.of(message1, message2);

        doReturn(true).when(this.chatRepository).existsByIdAndSponsor_Id(this.chat.getId(), this.sponsor.getId());
        doReturn(messages).when(this.messageRepository).findAllByChat_Id(this.chat.getId());

        List<MessageResponse> messagesByChatId = chatService.getMessagesByChatId(this.chat.getId());

        Assertions.assertEquals(messages.stream().map(MessageResponse::new).collect(Collectors.toList()),
                messagesByChatId);
    }

    @Test
    void getChatsByUserId() {
        Mockito.doReturn(List.of(this.chat))
                .when(chatRepository)
                .findAllBySponsor_Id(this.sponsor.getId());

        List<ChatResponse> chatsByUserId = chatService.getChatsByUserId();
        List<UUID> uuids = chatsByUserId.stream().map(ChatResponse::getChatId).toList();

        Assertions.assertEquals(List.of(this.chat.getId()), uuids);
    }
}