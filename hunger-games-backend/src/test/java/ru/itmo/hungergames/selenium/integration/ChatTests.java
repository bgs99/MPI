package ru.itmo.hungergames.selenium.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.integration.util.SeleniumIntegrationTestBase;
import ru.itmo.hungergames.selenium.pages.ChatPage;
import ru.itmo.hungergames.selenium.pages.MentorMenuPage;
import ru.itmo.hungergames.selenium.pages.SponsorMenuPage;
import ru.itmo.hungergames.selenium.pages.TributeMenuPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;

import java.util.Set;

@SeleniumTest
public class ChatTests extends SeleniumIntegrationTestBase {
    @Test
    @Sql(value = {
            "/initScripts/create-sponsor.sql",
            "/initScripts/create-mentor-with-tributes.sql",
            "/initScripts/create-resources.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void test() {
        this.getStartPage();

        final var sponsorUsername = "sponsor-name";
        final var sponsorPass = "pass";

        this.loginSponsor(sponsorUsername, sponsorPass);

        this.sponsorMenuGoTo(SponsorMenuPage.Action.Tributes);

        final var tributeName = "tribute-test1";
        this.sponsorSelectTribute(tributeName);

        this.sponsorTributeCreateChat();

        final var testMessage = "Test message";

        this.sendChatMessage(testMessage);

        final var chatPage = this.initPage(ChatPage.class);

        var chatMessages = chatPage.getMessages();

        Assertions.assertEquals(testMessage, chatMessages.get(chatMessages.size() - 1).getText());

        this.getStartPage();

        final var tributeUsername = "9667900f-24b2-4795-ad20-28b933d9ae32";
        this.loginCapitolUser(tributeUsername, UserRole.TRIBUTE);

        this.tributeMenuGoTo(TributeMenuPage.Action.Chats);

        final var sponsorName = "sponsor-test";
        final var mentorName = "mentor-test";

        final var participants = Set.of(tributeName, mentorName, sponsorName);

        this.selectChat(participants);

        chatMessages = chatPage.getMessages();

        Assertions.assertEquals(testMessage, chatMessages.get(chatMessages.size() - 1).getText());

        this.getStartPage();

        this.loginCapitolUser("1d3ad419-e98f-43f1-9ac6-08776036cded", UserRole.MENTOR);

        this.mentorMenuGoTo(MentorMenuPage.Action.CHATS);

        this.selectChat(participants);

        chatMessages = chatPage.getMessages();

        Assertions.assertEquals(testMessage, chatMessages.get(chatMessages.size() - 1).getText());
    }
}
