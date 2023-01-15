package ru.itmo.hungergames.selenium.integration.util;

import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.pages.CapitolAuthPage;
import ru.itmo.hungergames.selenium.pages.LoginPage;
import ru.itmo.hungergames.selenium.pages.RegisterPage;
import ru.itmo.hungergames.selenium.pages.SponsorMenuPage;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;

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

        this.redirectWait(() -> capitolAuthPage.login("name", userRole));
    }
}
