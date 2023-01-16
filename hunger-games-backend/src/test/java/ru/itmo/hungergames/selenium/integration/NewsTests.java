package ru.itmo.hungergames.selenium.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import ru.itmo.hungergames.selenium.integration.util.SeleniumIntegrationTestBase;
import ru.itmo.hungergames.selenium.pages.ModeratorMenuPage;
import ru.itmo.hungergames.selenium.pages.SponsorMenuPage;
import ru.itmo.hungergames.selenium.pages.SponsorNewsPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;

import java.util.List;
import java.util.stream.Collectors;

@SeleniumTest
public class NewsTests extends SeleniumIntegrationTestBase {
    @Test
    @Sql(value = {
            "/initScripts/create-sponsor.sql",
            "/initScripts/create-moderator.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void news() {
        this.getStartPage();

        final String sponsorUsername = "sponsor-name";
        final String sponsorPass = "pass";
        this.loginSponsor(sponsorUsername, sponsorPass);

        this.sponsorMenuGoTo(SponsorMenuPage.Action.Settings);

        this.sponsorSetEmail("email@example.com");

        this.sponsorSubscribe();

        this.getModeratorStartPage();

        this.loginModerator("moderator-username", "pass");

        this.moderatorMenuGoTo(ModeratorMenuPage.Action.News);

        final var title = "Test title";
        final var content = "Test content";

        this.moderatorPostNews(title, content);

        this.getStartPage();

        this.loginSponsor(sponsorUsername, sponsorPass);

        this.sponsorMenuGoTo(SponsorMenuPage.Action.News);

        final var sponsorNewsPage = this.initPage(SponsorNewsPage.class);

        final var actualNews = sponsorNewsPage.getEntries();

        final var actualTitles = actualNews.stream().map(SponsorNewsPage.NewsEntry::getTitle)
                .collect(Collectors.toList());
        Assertions.assertEquals(List.of(title), actualTitles);


        final var actualContents = actualNews.stream().map(SponsorNewsPage.NewsEntry::getContents)
                .collect(Collectors.toList());
        Assertions.assertEquals(List.of(content + "<br>"), actualContents);
    }
}
