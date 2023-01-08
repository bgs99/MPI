package ru.itmo.hungergames.selenium.unit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.pages.SponsorMenuPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

@SeleniumTest
public class SponsorMenuTests extends SeleniumTestBase {
    private SponsorMenuPage page;
    private String sponsorMenuUrl;

    @BeforeEach
    public void setUp() {
        var sponsor = User.builder()
                .id(new UUID(42, 42))
                .username("sponsor")
                .name("sponsor")
                .build();
        this.authenticate(sponsor, UserRole.SPONSOR);
        this.get("/sponsor");
        page = PageFactory.initElements(driver, SponsorMenuPage.class);
        sponsorMenuUrl = driver.getCurrentUrl();
    }

    private void testRedirect(WebElement button, String destination) {
        button.click();

        this.redirectWait(sponsorMenuUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith(destination));
    }

    @Test
    public void RedirectToChats() {
        testRedirect(page.getChatsButton(), "#/sponsor/chats");
    }


    @Test
    public void RedirectToTributes() {
        testRedirect(page.getTributesButton(), "#/sponsor/tributes");
    }

    @Test
    public void RedirectToNews() {
        testRedirect(page.getNewsButton(), "#/sponsor/news");
    }

    @Test
    public void RedirectToSettings() {
        testRedirect(page.getSettingsButton(), "#/sponsor/settings");
    }
}
