package ru.itmo.hungergames.selenium.integration;

import org.junit.jupiter.api.Test;
import ru.itmo.hungergames.selenium.integration.util.SeleniumIntegrationTestBase;
import ru.itmo.hungergames.selenium.util.SeleniumTest;

@SeleniumTest
public class SponsorRegistrationTests extends SeleniumIntegrationTestBase {
    @Test
    public void registration() {
        this.getStartPage();

        final String username = "test_user";
        final String password = "test_pass";

        this.registerSponsor("name", username, password);

        this.loginSponsor(username, password);

        this.assertUrlMatches("/sponsor");
    }
}
