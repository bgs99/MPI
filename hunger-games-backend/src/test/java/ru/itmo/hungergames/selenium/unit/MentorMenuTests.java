package ru.itmo.hungergames.selenium.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Moderator;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.pages.MentorMenuPage;
import ru.itmo.hungergames.selenium.pages.ModeratorMenuPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;

import java.util.Set;
import java.util.UUID;

@SeleniumTest
public class MentorMenuTests extends SeleniumTestBase {
    private MentorMenuPage page;

    @BeforeEach
    public void setUp() {
        var mentor = Mentor.builder()
                .id(new UUID(42, 42))
                .username("mentor")
                .name("mentor")
                .userRoles(Set.of(UserRole.MENTOR))
                .build();
        this.authenticate(mentor);
        this.get("/mentor");
        page = PageFactory.initElements(driver, MentorMenuPage.class);
    }

    @Test
    public void RedirectToSponsorOffers() {
        this.assertRedirects(() -> page.getConsiderSponsorOffers().click(), "/mentor/approval");
    }

    @Test
    public void RedirectToRequestResources() {
        this.assertRedirects(() -> page.getRequestResources().click(), "/mentor/tributes");
    }

    @Test
    public void RedirectToChats() {
        this.assertRedirects(() -> page.getChats().click(), "/mentor/chats");
    }
}
