package ru.itmo.hungergames.selenium.integration;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.integration.util.SeleniumIntegrationTestBase;
import ru.itmo.hungergames.selenium.util.SeleniumTest;

@SeleniumTest
public class AdvertisementTests extends SeleniumIntegrationTestBase {

    @Test
    @Sql(value = {
            "/initScripts/create-tribute.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {
            "/initScripts/drop-tribute.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void advertisement() {
        this.getStartPage();

        String tributeName = "tribute-name";

        this.loginCapitolUser(tributeName, UserRole.TRIBUTE);

        this.assertUrlMatches("/tribute");
    }

}
