package ru.itmo.hungergames.selenium.integration.util;

import ru.itmo.hungergames.model.entity.EventType;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.pages.*;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;

import java.time.Instant;

public class SeleniumIntegrationTestBase extends SeleniumTestBase {
    protected void getStartPage() {
        this.driver.get(this.composeUrl("/"));
    }

    protected void getModeratorStartPage() {
        this.driver.get(this.composeUrl("/moderator/login"));
    }

    protected void registerSponsor(String name, String username, String password) {
        final var loginPage = this.initPage(LoginPage.class);

        this.redirectWait(() -> loginPage.getSponsorRegisterButton().click());

        final var registerPage = this.initPage(RegisterPage.class);

        this.redirectWait(() -> registerPage.register(name, username, password));
    }

    protected void loginSponsor(String username, String password) {
        final var loginPage = this.initPage(LoginPage.class);

        this.redirectWait(() -> loginPage.login(username, password));
    }

    protected void sponsorMenuGoTo(SponsorMenuPage.Action action) {
        final var sponsorMenuPage = this.initPage(SponsorMenuPage.class);
        this.redirectWait(() -> sponsorMenuPage.goTo(action));
    }

    protected void sponsorSelectTribute(String tributeName) {
        final var page = this.initPage(SponsorTributesPage.class);

        this.redirectWait(() -> page.getRowByTributeName(tributeName).getSelectButton().click());
    }

    @Override
    protected void authenticate(User _user) {
        throw new RuntimeException("Auth mocks should not be used in integration tests");
    }

    @Override
    protected void get(String _destination) {
        throw new RuntimeException("Navigation by URLs should not be used in integration tests");
    }

    protected void loginCapitolUser(String name, UserRole userRole) {
        final var loginPage = this.initPage(LoginPage.class);

        this.redirectWait(() -> loginPage.getCapitolAuthLink().click());

        final var capitolAuthPage = this.initPage(CapitolAuthPage.class);

        this.redirectWait(() -> capitolAuthPage.login(name, userRole));
    }

    protected void tributeMenuGoTo(TributeMenuPage.Action action) {
        final var tributeMenuPage = this.initPage(TributeMenuPage.class);
        this.redirectWait(() -> tributeMenuPage.goTo(action));
    }

    protected void postAdvertisement(String advertisementText) {
        final var tributeCreateAdvertisementPage = this.initPage(TributeCreateAdvertisementPage.class);
        tributeCreateAdvertisementPage.getTextArea().sendKeys(advertisementText);
        tributeCreateAdvertisementPage.getPayButton().click();
    }

    protected void moderatorMenuGoTo(ModeratorMenuPage.Action action) {
        final var moderatorMenuPage = this.initPage(ModeratorMenuPage.class);

        this.redirectWait(() -> moderatorMenuPage.goTo(action));
    }

    protected void loginModerator(String username, String password) {
        final var loginPage = this.initPage(ModeratorLoginPage.class);

        this.redirectWait(() -> loginPage.login(username, password));
    }

    protected void addEvent(EventType type, String place, Instant time) {
        final var page = new TributeEventsPage(this.driver);
        this.waitForAngularRequests();
        page.addEvent(type, place, time);
    }

    protected void moderatorApprovePost() {
        final var moderatorReviewPage = this.initPage(ModeratorReviewPage.class);
        moderatorReviewPage.approve();
    }

    protected String moderatorApprovePostAndGet() {
        final var moderatorReviewPage = this.initPage(ModeratorReviewPage.class);
        String adHTML = moderatorReviewPage.getAdHTML();
        moderatorReviewPage.approve();
        return adHTML.replace("<br>", "");
    }

    protected String moderatorDenyPostAndGet() {
        final var moderatorReviewPage = this.initPage(ModeratorReviewPage.class);
        String adHTML = moderatorReviewPage.getAdHTML();
        moderatorReviewPage.deny();
        return adHTML.replace("<br>", "");
    }

}
