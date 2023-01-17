package ru.itmo.hungergames.selenium.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.repository.ResourceOrderRepository;
import ru.itmo.hungergames.selenium.integration.util.SeleniumIntegrationTestBase;
import ru.itmo.hungergames.selenium.pages.MentorMenuPage;
import ru.itmo.hungergames.selenium.pages.SponsorMenuPage;
import ru.itmo.hungergames.selenium.util.OrderDetailRepresentation;
import ru.itmo.hungergames.selenium.util.OrderRepresentation;
import ru.itmo.hungergames.selenium.util.SeleniumTest;

import java.util.List;

@SeleniumTest
public class MentorOrdersResourceTests extends SeleniumIntegrationTestBase {
    @Autowired
    private ResourceOrderRepository orderRepository;

    @Test
    @Sql(value = {
            "/initScripts/create-sponsor.sql",
            "/initScripts/create-mentor-with-tributes.sql",
            "/initScripts/create-resources.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void test() {
        this.getStartPage();

        this.loginCapitolUser("1d3ad419-e98f-43f1-9ac6-08776036cded", UserRole.MENTOR);

        this.mentorMenuGoTo(MentorMenuPage.Action.REQUEST);

        this.mentorSelectTribute("tribute-test1");

        final var order = new OrderRepresentation(List.of(
                new OrderDetailRepresentation("Resource-test-1", 42)
        ));

        this.mentorCreateOrder(order);

        this.getStartPage();

        this.loginSponsor("sponsor-name", "pass");

        this.sponsorMenuGoTo(SponsorMenuPage.Action.Tributes);

        this.sponsorSelectTribute("tribute-test1");

        this.sponsorTributePayOrder();

        this.sponsorPayOrder(order);

        final var allOrders = this.orderRepository.findAll();

        final var orderFound = allOrders.stream()
                .filter(order::matches)
                .findFirst().orElseThrow();

        Assertions.assertTrue(orderFound.isPaid());
        Assertions.assertTrue(orderFound.getApproved());
    }
}
