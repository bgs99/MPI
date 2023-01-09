package ru.itmo.hungergames.selenium.unit;

import com.paulhammant.ngwebdriver.NgWebDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.order.Resource;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.PaymentRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.selenium.pages.ResourcesPage;
import ru.itmo.hungergames.selenium.pages.SponsorCreateOrderPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.PaymentManagerService;
import ru.itmo.hungergames.service.ResourceService;
import ru.itmo.hungergames.service.SponsorService;
import ru.itmo.hungergames.service.TributeService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.*;

@SeleniumTest
public class SponsorCreateOrderPageTests extends SeleniumTestBase {

    @MockBean
    private SponsorService sponsorService;

    @MockBean
    private TributeService tributeService;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private PaymentManagerService paymentManagerService;

    private SponsorCreateOrderPage page;

    private final Mentor mentor = Mentor.builder()
            .district(1)
            .build();

    private final Tribute tribute = Tribute.builder()
            .id(new UUID(42, 42))
            .name("test")
            .mentor(this.mentor)
            .build();

    private final Resource resource1 = Resource.builder()
            .id(new UUID(42, 1))
            .name("resource-1")
            .price(new BigDecimal(42))
            .build();
    private final Resource resource2 = Resource.builder()
            .id(new UUID(42, 2))
            .name("resource-2")
            .price(new BigDecimal(43))
            .build();
    private final Resource resource3 = Resource.builder()
            .id(new UUID(42, 3))
            .name("resource-3")
            .price(new BigDecimal(44))
            .build();

    private final List<Resource> resources = List.of(this.resource1, this.resource2, this.resource3);

    private String sourceWindowHandle;

    private List<ResourcesPage.ResourceRow> resourceRows;

    private final UUID orderId = new UUID(42, 42);

    private final List<Integer> orderSize = List.of(0, 42, 43);

    @BeforeEach
    public void setUp() {
        var sponsor = User.builder()
                .id(new UUID(42, 42))
                .username("sponsor")
                .name("sponsor")
                .build();
        this.authenticate(sponsor, UserRole.SPONSOR);
        this.page = new SponsorCreateOrderPage(this.driver);

        this.sourceWindowHandle = this.driver.getWindowHandle();

        doReturn(new TributeResponse(this.tribute)).when(this.tributeService).getTributeById(this.tribute.getId());
        doReturn(List.of(resource1, resource2, resource3)).when(this.resourceService).getAllResources();

        this.get("/sponsor/tribute/" + this.tribute.getId() + "/createorder");
        PageFactory.initElements(driver, this.page);

        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver)driver);
        ngDriver.waitForAngularRequestsToFinish();

        this.resourceRows = this.page.getResourceRows();

        this.resourceRows.stream().map(ResourcesPage.ResourceRow::getAmountInput).forEach(WebElement::clear);

        var expectedOrderRequest = ResourceOrderRequest.builder()
                .tributeId(this.tribute.getId())
                .orderDetails(List.of(
                        new OrderDetailRequest(this.resource2.getId(), this.orderSize.get(1)),
                        new OrderDetailRequest(this.resource3.getId(), this.orderSize.get(2))
                ))
                .build();

        doReturn(ResourceOrderResponse.builder().orderId(this.orderId).build())
                .when(this.sponsorService).sendResourcesForApproval(expectedOrderRequest);
    }

    @AfterEach
    public void tearDown() {
        this.closePaymentWindows(this.sourceWindowHandle);
    }

    @Test
    public void showRecipient() {
        var expectedRecipient = this.tribute.getName();
        var actualRecipient = this.page.getRecipient().getText().substring("Получатель: ".length());

        Assertions.assertEquals(expectedRecipient, actualRecipient);
    }

    @Test
    public void listResources() {
        var expectedResourceNames = this.resources.stream().map(Resource::getName).collect(Collectors.toList());
        var actualResourceNames = this.resourceRows.stream().map(ResourcesPage.ResourceRow::getName).collect(Collectors.toList());

        Assertions.assertEquals(expectedResourceNames, actualResourceNames);


        var expectedResourcePrices = this.resources.stream().map(Resource::getPrice).collect(Collectors.toList());
        var actualResourcePrices = this.resourceRows.stream().map(ResourcesPage.ResourceRow::getUnitPrice).map(BigDecimal::new).collect(Collectors.toList());

        Assertions.assertEquals(expectedResourcePrices, actualResourcePrices);
    }

    private void enterOrder() {
        for (int i = 0; i < 3; ++i) {
            this.resourceRows.get(i).getAmountInput().sendKeys(this.orderSize.get(i).toString());
        }
    }

    @Test
    public void correctTotals() {
        this.enterOrder();

        Assertions.assertFalse(resourceRows.get(0).hasSum());

        final var expectedTotal2 = this.resource2.getPrice().multiply(new BigDecimal(this.orderSize.get(1)));

        Assertions.assertEquals(
                expectedTotal2,
                new BigDecimal(resourceRows.get(1).getSum()));

        final var expectedTotal3 = this.resource3.getPrice().multiply(new BigDecimal(this.orderSize.get(2)));

        Assertions.assertEquals(
                expectedTotal3,
                new BigDecimal(resourceRows.get(2).getSum()));

        final var expectedTotal = expectedTotal2.add(expectedTotal3);

        Assertions.assertEquals(
                expectedTotal,
                new BigDecimal(this.page.getTotal())
        );
    }

    @Test
    public void payOrderRedirect() {
        this.enterOrder();

        this.page.getPayButton().click();

        this.switchToNewWindow(this.sourceWindowHandle);

        assertThat(this.driver.getCurrentUrl(), endsWith("#/capitol/payment?id=" + orderId));
    }

    @Test
    public void payOrderApprove() {
        enterOrder();

        this.page.getPayButton().click();

        this.approvePayment(this.sourceWindowHandle);

        verify(this.paymentManagerService, times(1)).approvePayment(new PaymentRequest(orderId));

        Assertions.assertEquals("Оплата подтверждена", this.page.getPaymentStatus().getText());

        var sourceUrl = driver.getCurrentUrl();

        this.page.getGoBackButton().click();

        this.redirectWait(sourceUrl);

        assertThat(driver.getCurrentUrl(), endsWith("#/sponsor"));
    }

    @Test
    public void payOrderDeny() {
        enterOrder();

        this.page.getPayButton().click();

        this.denyPayment(this.sourceWindowHandle);

        verify(this.paymentManagerService, times(0)).approvePayment(new PaymentRequest(orderId));

        Assertions.assertEquals("Ошибка оплаты", this.page.getPaymentStatus().getText());
    }

    @Test
    public void payOrderDenyGoBack() {
        enterOrder();

        this.page.getPayButton().click();

        this.denyPayment(this.sourceWindowHandle);

        this.page.getGoBackButton().click();

        assertThat(driver.getCurrentUrl(), endsWith("#/sponsor"));
    }

    @Test
    public void payOrderDenyRetry() {
        enterOrder();

        this.page.getPayButton().click();

        this.denyPayment(this.sourceWindowHandle);

        this.page.getRetryButton().click();

        this.switchToNewWindow(this.sourceWindowHandle);

        assertThat(this.driver.getCurrentUrl(), endsWith("#/capitol/payment?id=" + orderId));
    }

    @Test
    public void payOrderDenyReselectResources() {
        enterOrder();

        this.page.getPayButton().click();

        this.denyPayment(this.sourceWindowHandle);

        this.page.getReselectResourcesButton().click();

        Assertions.assertTrue(this.page.getPayButton().isDisplayed());
    }


    @Test
    public void payOrderDenyReselectTribute() {
        enterOrder();

        this.page.getPayButton().click();

        this.denyPayment(this.sourceWindowHandle);

        final var sourceUrl = driver.getCurrentUrl();

        this.page.getReselectTributeButton().click();

        this.redirectWait(sourceUrl);

        assertThat(driver.getCurrentUrl(), endsWith("#/sponsor/tributes"));
    }

    @Test
    public void payOrderIgnore() {
        enterOrder();

        this.page.getPayButton().click();

        this.driver.switchTo().window(this.sourceWindowHandle);

        verify(this.paymentManagerService, times(0)).approvePayment(new PaymentRequest(orderId));

        Assertions.assertEquals("Ожидаем подтверждения оплаты", this.page.getPaymentStatus().getText());
    }

    @Test
    public void payOrderIgnoreGoBack() {
        enterOrder();

        this.page.getPayButton().click();

        this.driver.switchTo().window(this.sourceWindowHandle);

        this.page.getGoBackButton().click();

        assertThat(driver.getCurrentUrl(), endsWith("#/sponsor"));
    }

    @Test
    public void payOrderIgnoreRetry() {
        enterOrder();

        this.page.getPayButton().click();

        this.driver.switchTo().window(this.sourceWindowHandle);

        var oldWindows = this.driver.getWindowHandles();

        this.page.getRetryButton().click();

        this.switchToNewWindow(oldWindows);

        assertThat(this.driver.getCurrentUrl(), endsWith("#/capitol/payment?id=" + orderId));
    }
}
