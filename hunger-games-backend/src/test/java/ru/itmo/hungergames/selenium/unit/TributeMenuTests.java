package ru.itmo.hungergames.selenium.unit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.pages.TributeMenuPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

@SeleniumTest
public class TributeMenuTests extends SeleniumTestBase {
    private TributeMenuPage tributeMenuPage;
    private String tributeMenuUrl;

    @BeforeEach
    public void setUp() {
        var tribute = User.builder()
                .id(new UUID(42, 42))
                .username("tribute")
                .name("tribute")
                .build();
        this.authenticate(tribute, UserRole.TRIBUTE);
        this.get("/tribute");
        tributeMenuPage = PageFactory.initElements(driver, TributeMenuPage.class);
        tributeMenuUrl = driver.getCurrentUrl();
    }
    private void testRedirect(WebElement button, String destination) {
        button.click();

        this.redirectWait(tributeMenuUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith(destination));
    }

    @Test
    public void redirectToChats() {
        testRedirect(tributeMenuPage.getChatsButton(), "#/tribute/chats");
    }

    @Test
    public void redirectToEvents() {
        testRedirect(tributeMenuPage.getEventsButton(), "#/tribute/events");
    }

    @Test
    public void redirectToPosting() {
        testRedirect(tributeMenuPage.getAddPostButton(), "#/tribute/posting");
    }
}
