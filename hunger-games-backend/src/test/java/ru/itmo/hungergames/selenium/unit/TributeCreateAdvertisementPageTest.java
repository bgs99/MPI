package ru.itmo.hungergames.selenium.unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextOrderResponse;
import ru.itmo.hungergames.selenium.pages.TributeCreateAdvertisementPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.TributeService;
import ru.itmo.hungergames.util.ApplicationParameters;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.doReturn;

@SeleniumTest
public class TributeCreateAdvertisementPageTest extends SeleniumTestBase {

    @MockBean
    TributeService tributeService;


    private TributeCreateAdvertisementPage page;

    private String sourceWindowHandle;

    private final UUID orderId = new UUID(42, 42);

    private final String newAdvertisementText = "new advertisement";

    @BeforeEach
    public void setUp() {
        var tribute = User.builder()
                .id(new UUID(42, 42))
                .username("tribute")
                .name("tribute")
                .build();
        this.authenticate(tribute, UserRole.TRIBUTE);
        this.get("/tribute/posting");

        this.sourceWindowHandle = this.driver.getWindowHandle();

        this.page = PageFactory.initElements(driver, TributeCreateAdvertisementPage.class);

        var expectedOrderRequest = new AdvertisingTextRequest(newAdvertisementText + "<br>");

        doReturn(AdvertisingTextOrderResponse.builder()
                .orderId(orderId)
                .price(ApplicationParameters.advertisementOrderPrice)
                .build()
        )
                .when(tributeService).
                sendAdvertisingText(expectedOrderRequest);
    }

    @AfterEach
    public void tearDown() {
        this.closePaymentWindows(this.sourceWindowHandle);
    }

    @Test
    public void checkTotal() {
        Assertions.assertEquals(page.getTotal(), ApplicationParameters.advertisementOrderPrice.intValue());
    }


    @Test
    public void checkFormIsCleared() {
        this.page.getTextArea().sendKeys(newAdvertisementText);

        //Assertions.assertFalse(this.page.getTextArea().getText().isEmpty());
        this.page.getPayButton().click();

        Assertions.assertTrue(this.page.getTextArea().getText().isEmpty());
    }


    @Test
    public void payAdvertisementOrderRedirect() {
        this.page.getTextArea().sendKeys(newAdvertisementText);
        this.page.getPayButton().click();

        this.switchToNewWindow(this.sourceWindowHandle);

        assertThat(this.driver.getCurrentUrl(), endsWith("#/capitol/payment?id=" + orderId));
    }

}
