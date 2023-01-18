package ru.itmo.hungergames.selenium.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.chat.Chat;
import ru.itmo.hungergames.model.entity.chat.Message;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.response.ChatResponse;
import ru.itmo.hungergames.selenium.pages.ChatsPage;
import ru.itmo.hungergames.selenium.pages.MentorChatsPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.ChatService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.doReturn;

@SeleniumTest
public class MentorChatsPageTests extends SeleniumTestBase {
    private MentorChatsPage page;

    @MockBean
    private ChatService chatService;

    private final Mentor mentor = Mentor.builder()
            .id(new UUID(0, 42))
            .userRoles(Set.of(UserRole.MENTOR))
            .name("mentor1").build();

    private final Tribute tribute = Tribute.builder()
            .id(new UUID(42, 0))
            .name("tribute1")
            .mentor(mentor).build();

    private final Sponsor sponsor = Sponsor.builder()
            .id(new UUID(42, 42))
            .username("sponsor")
            .name("sponsor")
            .build();

    @BeforeEach
    public void setUp() {
        this.authenticate(this.mentor);
    }

    @Test
    public void chatsList() {
        var message1 = Message.builder()
                .message("message1")
                .dateTime(Instant.now().minus(Duration.ofDays(1)))
                .user(this.mentor)
                .build();
        var message2 = Message.builder()
                .message("message2")
                .dateTime(Instant.now().minus(Duration.ofHours(1)))
                .user(this.mentor)
                .build();

        var chat1 = Chat.builder()
                .tribute(tribute)
                .sponsor(sponsor)
                .id(new UUID(42, 0))
                .messages(List.of(message1, message2)).build();

        var chats = List.of(chat1);

        doReturn(chats.stream().map(ChatResponse::new).collect(Collectors.toList()))
                .when(chatService)
                .getChatsByUserId();

        this.page = this.getInit("/mentor/chats", MentorChatsPage.class);

        var chatRows = this.page.getChatRows();

        var names = chatRows.stream().map(ChatsPage.ChatRow::getParticipants).toList();
        var expectedNames = List.of(Set.of(tribute.getName(), sponsor.getName()));

        Assertions.assertEquals(expectedNames, names);

        var messages = chatRows.stream().map(ChatsPage.ChatRow::getLastMessage).collect(Collectors.toList());
        var expectedMessages = List.of(message2.getMessage());

        Assertions.assertEquals(expectedMessages, messages);
    }

    @Test
    public void redirectToChat() {
        var mentor = Mentor.builder().id(new UUID(0, 42)).name("mentor1").build();
        var tribute = Tribute.builder()
                .id(new UUID(42, 0))
                .name("tribute1")
                .mentor(mentor).build();

        var chat = Chat.builder()
                .tribute(tribute)
                .sponsor(sponsor)
                .id(new UUID(42, 0))
                .messages(List.of()).build();


        doReturn(List.of(new ChatResponse(chat)))
                .when(chatService)
                .getChatsByUserId();


        this.page = this.getInit("/mentor/chats", MentorChatsPage.class);

        var chatRow = this.page.getChatRows().get(0);

        this.assertRedirects(chatRow::select, "/mentor/chat/" + chat.getId().toString());
    }
}
