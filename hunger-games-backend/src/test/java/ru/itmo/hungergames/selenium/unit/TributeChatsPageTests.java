package ru.itmo.hungergames.selenium.unit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.chat.Chat;
import ru.itmo.hungergames.model.entity.chat.Message;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.response.ChatResponse;
import ru.itmo.hungergames.selenium.pages.ChatsPage;
import ru.itmo.hungergames.selenium.pages.TributeChatsPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.ChatService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

@SeleniumTest
public class TributeChatsPageTests extends SeleniumTestBase {

    private TributeChatsPage page;

    @MockBean
    private ChatService chatService;

    private final Mentor mentor = Mentor.builder()
            .id(new UUID(0, 42))
            .name("mentor")
            .build();

    private final Tribute self = Tribute.builder()
            .id(new UUID(42, 42))
            .name("tribute")
            .username("test")
            .password("test")
            .userRoles(Set.of(UserRole.TRIBUTE))
            .mentor(mentor)
            .build();

    @BeforeEach
    public void setUp() {
        this.authenticate(this.self, UserRole.TRIBUTE);
    }

    @Test
    public void chatsList() {
        var sponsor1 = Sponsor.builder()
                .id(new UUID(42, 0))
                .name("sponsor1")
                .build();
        var sponsor2 = Sponsor.builder()
                .id(new UUID(42, 1))
                .name("sponsor2")
                .build();

        var message1 = Message.builder()
                .message("message1")
                .dateTime(Instant.now().minus(Duration.ofDays(1)))
                .user(this.self)
                .build();
        var message2 = Message.builder()
                .message("message2")
                .dateTime(Instant.now().minus(Duration.ofHours(1)))
                .user(this.self)
                .build();

        var chat1 = Chat.builder()
                .tribute(this.self)
                .sponsor(sponsor1)
                .id(new UUID(42, 0))
                .messages(List.of(message1)).build();


        var chat2 = Chat.builder()
                .tribute(this.self)
                .sponsor(sponsor2)
                .id(new UUID(42, 1))
                .messages(List.of(message2)).build();

        var chats = List.of(chat1, chat2);

        doReturn(chats.stream().map(ChatResponse::new).collect(Collectors.toList()))
                .when(chatService)
                .getChatsByUserId();

        this.page = this.getInit("/tribute/chats", TributeChatsPage.class);

        var chatRows = this.page.getChatRows();

        var names = chatRows.stream().map(ChatsPage.ChatRow::getSelectButton).map(WebElement::getText).collect(Collectors.toList());
        var expectedNames = List.of(mentor.getName() + ", " + sponsor1.getName(), mentor.getName() + ", " + sponsor2.getName());

        Assertions.assertEquals(expectedNames, names);

        var messages = chatRows.stream().map(ChatsPage.ChatRow::getLastMessage).collect(Collectors.toList());
        var expectedMessages = List.of(message1.getMessage(), message2.getMessage());

        Assertions.assertEquals(expectedMessages, messages);
    }

    @Test
    public void redirectToChat() {
        var sponsor = Sponsor.builder()
                .id(new UUID(42, 0))
                .name("sponsor")
                .build();

        var chat = Chat.builder()
                .tribute(this.self)
                .sponsor(sponsor)
                .id(new UUID(42, 0))
                .messages(List.of()).build();


        doReturn(List.of(new ChatResponse(chat)))
                .when(chatService)
                .getChatsByUserId();


        this.page = this.getInit("/tribute/chats", TributeChatsPage.class);

        var chatRow = this.page.getChatRows().get(0);

        var sourceUrl = driver.getCurrentUrl();

        chatRow.getSelectButton().click();

        this.redirectWait(sourceUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#/tribute/chat/" + chat.getId().toString()));
    }
}
