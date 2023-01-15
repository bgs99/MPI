package ru.itmo.hungergames.selenium.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;
import ru.itmo.hungergames.model.entity.user.Moderator;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.pages.ModeratorMenuPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;

import java.util.Set;
import java.util.UUID;


@SeleniumTest
public class ModeratorMenuTests extends SeleniumTestBase {
    private ModeratorMenuPage page;

    @BeforeEach
    public void setUp() {
        var moderator = Moderator.builder()
                .id(new UUID(42, 42))
                .username("moderator")
                .name("moderator")
                .userRoles(Set.of(UserRole.MODERATOR))
                .build();
        this.authenticate(moderator);
        this.get("/moderator");
        page = PageFactory.initElements(driver, ModeratorMenuPage.class);
    }

    @Test
    public void RedirectToNews() {
        this.assertRedirects(() -> page.getAddNewsButton().click(), "/moderator/post");
    }

    @Test
    public void RedirectToPosts() {
        this.assertRedirects(() -> page.getTributesButton().click(), "/moderator/review");
    }
}
