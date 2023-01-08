package ru.itmo.hungergames.selenium.unit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.chat.Chat;
import ru.itmo.hungergames.model.entity.chat.Message;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.response.ChatResponse;
import ru.itmo.hungergames.selenium.pages.ChatsPage;
import ru.itmo.hungergames.selenium.pages.SponsorChatsPage;
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
public class SponsorChatsPageTests extends SeleniumTestBase {
    private SponsorChatsPage page;

    @MockBean
    private ChatService chatService;


    private final Sponsor self = Sponsor.builder()
            .id(new UUID(42, 42))
            .name("sponsor")
            .username("test")
            .password("test")
            .userRoles(Set.of(UserRole.SPONSOR))
            .build();

    @BeforeEach
    public void setUp() {
        this.page = new SponsorChatsPage(this.driver);

        this.authenticate(this.self, UserRole.SPONSOR);
    }

    @Test
    public void chatsList() {
        var mentor1 = Mentor.builder().id(new UUID(0, 42)).name("mentor1").build();
        var mentor2 = Mentor.builder().id(new UUID(1, 42)).name("mentor1").build();
        var tribute1 = Tribute.builder()
                .id(new UUID(42, 0))
                .name("tribute1")
                .mentor(mentor1).build();
        var tribute2 = Tribute.builder()
                .id(new UUID(42, 1))
                .name("tribute2")
                .mentor(mentor2).build();

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
                .tribute(tribute1)
                .sponsor(this.self)
                .id(new UUID(42, 0))
                .messages(List.of(message1)).build();


        var chat2 = Chat.builder()
                .tribute(tribute2)
                .sponsor(this.self)
                .id(new UUID(42, 1))
                .messages(List.of(message2)).build();

        var chats = List.of(chat1, chat2);

        doReturn(chats.stream().map(ChatResponse::new).collect(Collectors.toList()))
                .when(chatService)
                .getChatsByUserId();


        this.get("/sponsor/chats");

        page.waitUntilChatsLoaded();

        var chatRows = this.page.getChatRows();

        var names = chatRows.stream().map(ChatsPage.ChatRow::getSelectButton).map(WebElement::getText).collect(Collectors.toList());
        var expectedNames = List.of(tribute1.getName() + ", " + mentor1.getName(), tribute2.getName() + ", " + mentor2.getName());

        Assertions.assertEquals(expectedNames, names);

        var messages = chatRows.stream().map(ChatsPage.ChatRow::getLastMessage).collect(Collectors.toList());
        var expectedMessages = List.of(message1.getMessage(), message2.getMessage());

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
                .sponsor(this.self)
                .id(new UUID(42, 0))
                .messages(List.of()).build();


        doReturn(List.of(new ChatResponse(chat)))
                .when(chatService)
                .getChatsByUserId();


        this.get("/sponsor/chats");

        page.waitUntilChatsLoaded();

        var chatRow = this.page.getChatRows().get(0);

        var sourceUrl = driver.getCurrentUrl();

        chatRow.getSelectButton().click();

        this.redirectWait(sourceUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#/sponsor/chat/" + chat.getId().toString()));
    }


    @Test
    public void createChatRedirect() {
        doReturn(List.of())
                .when(chatService)
                .getChatsByUserId();

        this.get("/sponsor/chats");

        PageFactory.initElements(driver, this.page);

        var sourceUrl = driver.getCurrentUrl();

        page.newChatButton.click();

        this.redirectWait(sourceUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#/sponsor/tributes"));
    }
}
