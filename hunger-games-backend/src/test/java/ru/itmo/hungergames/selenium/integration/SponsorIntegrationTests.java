package ru.itmo.hungergames.selenium.integration;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import ru.itmo.hungergames.selenium.integration.util.SeleniumIntegrationTestBase;
import ru.itmo.hungergames.selenium.pages.SponsorMenuPage;
import ru.itmo.hungergames.selenium.pages.SponsorNewsPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;

@SeleniumTest
public class SponsorIntegrationTests extends SeleniumIntegrationTestBase {
    @Test
    public void registration() {
        this.getStartPage();

        final String username = "test_user";
        final String password = "test_pass";

        this.registerSponsor("name", username, password);

        this.loginSponsor(username, password);

        this.assertUrlMatches("/sponsor");
    }

    @Test
    @Sql(value = {
            "/initScripts/create-sponsor-with-subscription.sql",
            "/initScripts/create-moderators-and-news.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {
            "/initScripts/drop-sponsor-with-subscription.sql",
            "/initScripts/drop-moderators-and-news.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void news() {
        this.getStartPage();

        final String sponsorUsername = "sponsor-name";
        final String sponsorPass = "pass";
        this.loginSponsor(sponsorUsername, sponsorPass);

        this.sponsorMenuGoTo(SponsorMenuPage.Action.News);

        var expectedTitles = List.of("name1", "name2", "name3", "name4");

        final var sponsorNewsPage = this.initPage(SponsorNewsPage.class);

        var actualTitles = sponsorNewsPage.getEntries().stream().map(SponsorNewsPage.NewsEntry::getTitle)
                .collect(Collectors.toList());

        Assertions.assertEquals(expectedTitles, actualTitles);
    }
}
