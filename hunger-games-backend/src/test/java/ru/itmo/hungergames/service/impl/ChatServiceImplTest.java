package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.exception.ChatExistsException;
import ru.itmo.hungergames.model.entity.chat.Chat;
import ru.itmo.hungergames.model.entity.chat.Message;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.ChatCreateRequest;
import ru.itmo.hungergames.model.request.MessageRequest;
import ru.itmo.hungergames.model.response.ChatResponse;
import ru.itmo.hungergames.model.response.MessageResponse;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @SpyBean
    ChatRepository chatRepository;
    @SpyBean
    MessageRepository messageRepository;
    @Test
    @Sql(value = {"/initScripts/create-chat-users.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-chat-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createChat() {
        UUID sponsorUUID = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        UUID tributeUUID = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Tribute tribute = new Tribute();
        tribute.setId(tributeUUID);
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorUUID);

        Mockito.doReturn(Optional.of(tribute))
                .when(tributeRepository)
                .findById(tributeUUID);
        Mockito.doReturn(Optional.of(sponsor))
                .when(sponsorRepository)
                .findById(sponsorUUID);
        Mockito.doReturn(sponsorUUID)
                .when(securityUtil)
                .getAuthenticatedUserId();

        ChatResponse chatResponse = chatService.createChat(new ChatCreateRequest(tributeUUID));
        Chat chat = chatRepository.findById(chatResponse.getChatId()).orElseThrow();

        Mockito.verify(chatRepository, Mockito.times(1)).save(ArgumentMatchers.any(Chat.class));
        Mockito.clearInvocations(chatRepository);

        assertEquals(chat.getId(), chatResponse.getChatId());
        assertEquals(tributeUUID, chat.getTribute().getId());
        assertEquals(sponsorUUID, chat.getSponsor().getId());
        chatRepository.delete(chat);
    }
    @Test
    @Sql(value = {"/initScripts/create-chat-users.sql", "/initScripts/create-chat.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-chat.sql", "/initScripts/drop-chat-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void validateBeforeCreateFailure(){
        UUID sponsorUUID = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        UUID tributeUUID = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Mockito.doReturn(sponsorUUID).when(securityUtil).getAuthenticatedUserId();

        Throwable thrown = catchThrowable(()-> chatService.createChat(new ChatCreateRequest(tributeUUID)));

        assertThat(thrown).isInstanceOf(ChatExistsException.class);
        assertThat(thrown.getMessage()).contains(String.format("Chat for tribute with id=%s and sponsor with id=%s already exists.", tributeUUID, sponsorUUID));
    }

    @Test
    @Sql(value = {"/initScripts/create-chat-users.sql", "/initScripts/create-chat.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-chat.sql", "/initScripts/drop-chat-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendMessage() {
        UUID userUUID = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        User user = new User();
        user.setId(userUUID);
        UUID chatUUID = UUID.fromString("c0fc15d4-d065-4ca8-b985-2857f1d2d0c5");
        String textMessage = "Some message text";

        Mockito.doReturn(user)
                .when(securityUtil)
                .getAuthenticatedUser();

        MessageResponse messageResponse = chatService.sendMessage(chatUUID, new MessageRequest(textMessage));
        Message message = messageRepository.findById(messageResponse.getId()).orElseThrow();

        Mockito.verify(messageRepository, Mockito.times(1)).save(ArgumentMatchers.any(Message.class));
        Mockito.clearInvocations(messageRepository);

        assertEquals(userUUID, message.getUser().getId());
        assertEquals(textMessage, messageResponse.getMessage());
        assertNotNull(messageResponse.getDateTime());

        messageRepository.delete(message);
    }

    @Test
    @Sql(value = {"/initScripts/create-chat-users.sql", "/initScripts/create-chat.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-chat.sql", "/initScripts/drop-chat-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getMessagesByChatId() {
        UUID sponsorUUID = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        UUID chatUUID = UUID.fromString("c0fc15d4-d065-4ca8-b985-2857f1d2d0c5");
        List<UUID> initialUUIDs = new ArrayList<>();
        initialUUIDs.add(UUID.fromString("e71f2e9b-f4be-4ae7-94b3-7ba1c2b9d8dd"));
        initialUUIDs.add(UUID.fromString("336426af-cd62-49db-893c-f085d389a9bc"));
        initialUUIDs.add(UUID.fromString("1c20827a-2242-47dc-900b-a52f14d139fc"));
        initialUUIDs.add(UUID.fromString("2def889f-38e3-4495-b929-36b8870ebca2"));
        initialUUIDs.add(UUID.fromString("d0f5e7f3-b008-499d-9e4c-8a1fa32fd111"));
        initialUUIDs.add(UUID.fromString("e240b26d-edb7-4c7e-a92c-6b850fa31fe5"));

        Mockito.doReturn(sponsorUUID).when(securityUtil).getAuthenticatedUserId();
        Mockito.doReturn(UserRole.SPONSOR).when(securityUtil).getAuthenticatedUserRole();

        List<MessageResponse> messagesByChatId = chatService.getMessagesByChatId(chatUUID);
        List<UUID> uuids = messagesByChatId.stream().map(MessageResponse::getId).toList();

        assertTrue(uuids.containsAll(initialUUIDs));
        Mockito.verify(messageRepository, Mockito.times(1)).findAllByChat_Id(chatUUID);
    }

    @Test
    @Sql(value = {"/initScripts/create-chat-users.sql", "/initScripts/create-chat.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-chat.sql", "/initScripts/drop-chat-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getChatsByUserId() {
        UUID sponsorUUID = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        UUID chatUUID = UUID.fromString("c0fc15d4-d065-4ca8-b985-2857f1d2d0c5");
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorUUID);

        Mockito.doReturn(Optional.of(sponsor))
                .when(sponsorRepository)
                .findById(sponsorUUID);
        Mockito.doReturn(sponsor)
                .when(securityUtil)
                .getAuthenticatedUser();
        Mockito.doReturn(sponsorUUID)
                .when(securityUtil)
                .getAuthenticatedUserId();
        Mockito.doReturn(UserRole.SPONSOR)
                .when(securityUtil)
                .getAuthenticatedUserRole();

        List<ChatResponse> chatsByUserId = chatService.getChatsByUserId();
        List<UUID> uuids = chatsByUserId.stream().map(ChatResponse::getChatId).toList();

        assertTrue(uuids.contains(chatUUID));
    }
}