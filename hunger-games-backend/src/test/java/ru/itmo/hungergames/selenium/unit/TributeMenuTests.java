package ru.itmo.hungergames.selenium.unit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.pages.TributeMenuPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.TestAuthData;
import ru.itmo.hungergames.selenium.util.Utils;

import static org.hamcrest.MatcherAssert.assertThat;

@SeleniumTest(relativeUrl = "/tribute")
public class TributeMenuTests {
    @Autowired
    private WebDriver driver;

    private TributeMenuPage page;
    private String tributeMenuUrl;

    @BeforeEach
    public void setUp() {
        page = PageFactory.initElements(driver, TributeMenuPage.class);
        tributeMenuUrl = driver.getCurrentUrl();
    }

    private void testRedirect(WebElement button, String destination) {
        button.click();

        Utils.redirectWait(driver, tributeMenuUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith(destination));
    }

    @Test
    @TestAuthData(role = UserRole.TRIBUTE)
    public void redirectToChats() {
        testRedirect(page.getChatsButton(), "#/tribute/chats");
    }

    @Test
    @TestAuthData(role = UserRole.TRIBUTE)
    public void redirectToEvents() {
        testRedirect(page.getEventsButton(), "#/tribute/events");
    }

    @Test
    @TestAuthData(role = UserRole.TRIBUTE)
    public void redirectToPosting() {
        testRedirect(page.getAddPostButton(), "#/tribute/posting");
    }
}
