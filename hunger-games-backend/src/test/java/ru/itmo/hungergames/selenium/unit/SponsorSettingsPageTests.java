package ru.itmo.hungergames.selenium.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.NewsSubscriptionOrderRequest;
import ru.itmo.hungergames.model.request.PaymentRequest;
import ru.itmo.hungergames.model.request.SettingsChangeRequest;
import ru.itmo.hungergames.model.response.NewsSubscriptionOrderResponse;
import ru.itmo.hungergames.model.response.SettingsResponse;
import ru.itmo.hungergames.selenium.pages.SponsorSettingsPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.PaymentManagerService;
import ru.itmo.hungergames.service.SponsorService;
import ru.itmo.hungergames.service.UserService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SeleniumTest
public class SponsorSettingsPageTests extends SeleniumTestBase {
    private SponsorSettingsPage page;

    @MockBean
    private UserService userService;

    @MockBean
    private SponsorService sponsorService;

    @MockBean
    private PaymentManagerService paymentManagerService;

    private String sourceWindow;

    private final BigDecimal subscriptionPrice = new BigDecimal(42);

    @BeforeEach
    public void setUp() {
        var sponsor = User.builder()
                .id(new UUID(42, 42))
                .username("sponsor")
                .name("sponsor")
                .build();
        this.authenticate(sponsor, UserRole.SPONSOR);
        this.page = PageFactory.initElements(driver, SponsorSettingsPage.class);
        this.sourceWindow = this.driver.getWindowHandle();

        doReturn(this.subscriptionPrice).when(this.sponsorService).getPriceOfNewsSubscription();
    }

    @Test
    public void oldEmailEmpty() {
        doReturn(SettingsResponse.builder().email(null).build()).when(this.userService).getSettings();
        doReturn(false).when(this.sponsorService).isSubscribed();

        this.get("/sponsor/settings");

        this.waitForAngularRequests();

        Assertions.assertEquals("", this.page.getOldEmail());

        Assertions.assertFalse(this.page.canSubscribe());
    }

    @Test
    public void changeEmailCorrect() {
        final var newEmail = "example@example.com";

        doReturn(SettingsResponse.builder().email(null).build()).when(this.userService).getSettings();
        doNothing().when(this.userService).changeSettings(new SettingsChangeRequest(newEmail));
        doReturn(false).when(this.sponsorService).isSubscribed();

        this.get("/sponsor/settings");

        this.waitForAngularRequests();

        doReturn(SettingsResponse.builder().email(newEmail).build()).when(this.userService).getSettings();

        this.page.getNewEmailInput().sendKeys(newEmail);
        this.page.getChangeEmailButton().click();

        verify(this.userService, times(1)).changeSettings(new SettingsChangeRequest(newEmail));

        this.waitForAngularRequests();

        Assertions.assertEquals(newEmail, this.page.getOldEmail());

        Assertions.assertTrue(this.page.canSubscribe());
    }

    @Test
    public void changeEmailIncorrect() {
        final var newEmail = "test";

        doReturn(SettingsResponse.builder().email(null).build()).when(this.userService).getSettings();
        doThrow(new RuntimeException()).when(this.userService).changeSettings(new SettingsChangeRequest(newEmail));
        doReturn(false).when(this.sponsorService).isSubscribed();

        this.get("/sponsor/settings");

        this.waitForAngularRequests();

        this.page.getNewEmailInput().sendKeys(newEmail);
        this.page.getChangeEmailButton().click();

        verify(this.userService, times(1)).changeSettings(new SettingsChangeRequest(newEmail));

        this.waitForAngularRequests();

        Assertions.assertEquals("", this.page.getOldEmail());

        Assertions.assertFalse(this.page.canSubscribe());
    }

    @Test
    public void hasEmailAndNotSubscribed() {
        doReturn(SettingsResponse.builder().email("example@example.com").build()).when(this.userService).getSettings();
        doReturn(false).when(this.sponsorService).isSubscribed();

        this.get("/sponsor/settings");

        this.waitForAngularRequests();

        Assertions.assertTrue(this.page.canSubscribe());

        Assertions.assertEquals(this.subscriptionPrice, this.page.getSubscriptionPrice());
    }

    @Test
    public void hasEmailButAlreadySubscribed() {
        doReturn(SettingsResponse.builder().email("example@example.com").build()).when(this.userService).getSettings();
        doReturn(true).when(this.sponsorService).isSubscribed();

        this.get("/sponsor/settings");

        this.waitForAngularRequests();

        Assertions.assertFalse(this.page.canSubscribe());
    }

    @Test
    public void subscribe() {
        final var email = "example@example.com";
        doReturn(SettingsResponse.builder().email(email).build()).when(this.userService).getSettings();
        doReturn(false).when(this.sponsorService).isSubscribed();

        this.get("/sponsor/settings");

        this.waitForAngularRequests();

        final var orderId = new UUID(42, 42);
        doReturn(new NewsSubscriptionOrderResponse(orderId)).when(this.sponsorService).subscribeToNews(new NewsSubscriptionOrderRequest(email));

        this.page.subscribe();

        doNothing().when(this.paymentManagerService).approvePayment(new PaymentRequest(orderId));

        this.approvePayment(this.sourceWindow);

        this.waitForAngularRequests();
        verify(this.paymentManagerService, times(1)).approvePayment(new PaymentRequest(orderId));
    }
}
