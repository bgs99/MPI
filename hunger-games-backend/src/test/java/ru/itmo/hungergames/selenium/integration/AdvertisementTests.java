package ru.itmo.hungergames.selenium.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.repository.AdvertisementOrderRepository;
import ru.itmo.hungergames.selenium.integration.util.SeleniumIntegrationTestBase;
import ru.itmo.hungergames.selenium.pages.ModeratorMenuPage;
import ru.itmo.hungergames.selenium.pages.SponsorMenuPage;
import ru.itmo.hungergames.selenium.pages.SponsorTributePage;
import ru.itmo.hungergames.selenium.pages.TributeMenuPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SeleniumTest
public class AdvertisementTests extends SeleniumIntegrationTestBase {

    @Autowired
    AdvertisementOrderRepository advertisementOrderRepository;

    @Test
    @Sql(value = {
            "/initScripts/create-tribute.sql",
            "/initScripts/create-moderators-and-news.sql",
            "/initScripts/create-sponsor.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void advertisement() {
        this.getStartPage();

        final String tributeUserName = "tribute-name";
        final String tributeName = "tribute-test";

        this.loginCapitolUser(tributeUserName, UserRole.TRIBUTE);

        this.tributeMenuGoTo(TributeMenuPage.Action.Post);

        List<String> expectedAdvertisementTexts = List.of(
                "Integration test advertisement text 1",
                "Integration test advertisement text 2",
                "Integration test advertisement text 3"
        );

        for (String advertisementText :
                expectedAdvertisementTexts) {
            this.postAdvertisement(advertisementText);
            this.approvePayment(this.driver.getWindowHandle());
        }

        this.getModeratorStartPage();

        final String moderatorUsername = "76f1316a-3eec-46c6-a323-bf3060a3c72a";
        final String moderatorPass = "pass";

        this.loginModerator(moderatorUsername, moderatorPass);

        this.moderatorMenuGoTo(ModeratorMenuPage.Action.Tributes);

        List<String> approvedAdvertisements = List.of(
                this.moderatorApprovePostAndGet(),
                this.moderatorApprovePostAndGet()
        );

        String deniedAdvertisement = this.moderatorDenyPostAndGet();

        this.getStartPage();

        final String sponsorUsername = "sponsor-name";
        final String sponsorPass = "pass";
        this.loginSponsor(sponsorUsername, sponsorPass);

        this.sponsorMenuGoTo(SponsorMenuPage.Action.Tributes);

        this.sponsorSelectTribute(tributeName);

        final var tributePage = this.initPage(SponsorTributePage.class);

        var posts = tributePage.getPosts().stream().map(WebElement::getText).collect(Collectors.toList());
        Collections.reverse(posts);

        Assertions.assertTrue(posts.containsAll(approvedAdvertisements));
        Assertions.assertFalse(posts.contains(deniedAdvertisement));
    }

}
