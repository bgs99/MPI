package ru.itmo.hungergames.selenium.integration;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import ru.itmo.hungergames.model.entity.EventType;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.integration.util.SeleniumIntegrationTestBase;
import ru.itmo.hungergames.selenium.pages.SponsorMenuPage;
import ru.itmo.hungergames.selenium.pages.SponsorTributePage;
import ru.itmo.hungergames.selenium.pages.TributeMenuPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;

@SeleniumTest
public class EventsTests extends SeleniumIntegrationTestBase {
    @Test
    @Sql(value = {
            "/initScripts/create-sponsor.sql",
            "/initScripts/create-tribute.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addEvent() {
        this.getStartPage();

        final var tributeUsername = "tribute-name";
        final var tributeName = "tribute-test";

        this.loginCapitolUser(tributeUsername, UserRole.TRIBUTE);

        this.tributeMenuGoTo(TributeMenuPage.Action.Events);

        final var eventType = EventType.INTERVIEW;
        final var place = "test place";
        final var time = Instant.now().plus(Duration.ofDays(1)).truncatedTo(ChronoUnit.SECONDS);

        this.addEvent(eventType, place, time);

        this.getStartPage();

        this.loginSponsor("sponsor-name", "pass");

        this.sponsorMenuGoTo(SponsorMenuPage.Action.Tributes);

        this.sponsorSelectTribute(tributeName);

        final var tributePage = this.initPage(SponsorTributePage.class);

        final var eventCard = tributePage.getEventCards().get(0);

        Assertions.assertEquals(place, eventCard.getPlace());
        Assertions.assertEquals(eventType.humanReadable(), eventCard.getEventType());
        Assertions.assertEquals(time, eventCard.getEventDate());

    }
}
