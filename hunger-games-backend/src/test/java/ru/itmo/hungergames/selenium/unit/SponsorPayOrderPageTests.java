package ru.itmo.hungergames.selenium.unit;

import com.paulhammant.ngwebdriver.NgWebDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.order.OrderDetail;
import ru.itmo.hungergames.model.entity.order.Resource;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.PaymentRequest;
import ru.itmo.hungergames.model.response.ResourceApprovedAndNotPaidResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.selenium.pages.SponsorPayOrderPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.*;

@SeleniumTest
public class SponsorPayOrderPageTests extends SeleniumTestBase {

    @MockBean
    private SponsorService sponsorService;

    @MockBean
    private TributeService tributeService;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private PaymentManagerService paymentManagerService;

    private SponsorPayOrderPage page;

    private final Mentor mentor = Mentor.builder()
            .district(1)
            .build();

    private final Tribute tribute = Tribute.builder()
            .id(new UUID(42, 42))
            .name("test")
            .mentor(this.mentor)
            .build();

    private final Resource resource1 = Resource.builder()
            .name("resource-1")
            .price(new BigDecimal(42))
            .build();
    private final Resource resource2 = Resource.builder()
            .name("resource-2")
            .price(new BigDecimal(43))
            .build();
    private final Resource resource3 = Resource.builder()
            .name("resource-3")
            .price(new BigDecimal(44))
            .build();

    private String sourceWindowHandle;

    @BeforeEach
    public void setUp() {
        var sponsor = User.builder()
                .id(new UUID(42, 42))
                .username("sponsor")
                .name("sponsor")
                .build();
        this.authenticate(sponsor, UserRole.SPONSOR);

        this.sourceWindowHandle = this.driver.getWindowHandle();

        doReturn(new TributeResponse(this.tribute)).when(this.tributeService).getTributeById(this.tribute.getId());
        doReturn(List.of(resource1, resource2, resource3)).when(this.resourceService).getAllResources();
    }

    @AfterEach
    public void tearDown() {
        this.closePaymentWindows(this.sourceWindowHandle);
    }

    @Test
    public void listOrders() {
        var orderDetail1 = OrderDetail.builder()
                .resource(this.resource1)
                .size(42)
                .build();
        var orderDetail2 = OrderDetail.builder()
                .resource(this.resource2)
                .size(43)
                .build();
        var orderDetail3 = OrderDetail.builder()
                .resource(this.resource3)
                .size(44)
                .build();

        var order1 = ResourceOrder.builder()
                .orderDetails(List.of(orderDetail1, orderDetail2))
                .tribute(this.tribute)
                .build();
        var order2 = ResourceOrder.builder()
                .orderDetails(List.of(orderDetail3))
                .tribute(this.tribute)
                .build();

        doReturn(Stream.of(order1, order2).map(ResourceApprovedAndNotPaidResponse::new).collect(Collectors.toList()))
                .when(this.sponsorService)
                .getOrdersNotPaidAndApproved();

        this.get("/sponsor/tribute/" + this.tribute.getId() + "/payorder");
        this.page = PageFactory.initElements(driver, SponsorPayOrderPage.class);

        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver)driver);
        ngDriver.waitForAngularRequestsToFinish();

        var expectedDetails1 = List.of(orderDetail1.getSize() + "X " + this.resource1.getName(), orderDetail2.getSize() + "X " + this.resource2.getName());
        var expectedDetails2 = List.of(orderDetail3.getSize() + "X " + this.resource3.getName());

        var orderRows = this.page.getOrderRows();
        Assertions.assertEquals(2, orderRows.size());

        var orderRow1 = this.page.getOrderRows().get(0);
        var orderRow2 = this.page.getOrderRows().get(1);

        Assertions.assertEquals(expectedDetails1, orderRow1.getDetails());
        Assertions.assertEquals(expectedDetails2, orderRow2.getDetails());

        var expectedSum1 = orderDetail1.getTotal().add(orderDetail2.getTotal());
        var expectedSum2 = orderDetail3.getTotal();

        Assertions.assertEquals(expectedSum1, new BigDecimal(orderRow1.getSum()));
        Assertions.assertEquals(expectedSum2, new BigDecimal(orderRow2.getSum()));

        Assertions.assertTrue(orderRow1.getPayButton().isDisplayed());
        Assertions.assertTrue(orderRow2.getPayButton().isDisplayed());
    }

    public UUID prepareOrder() {
        var orderDetail = OrderDetail.builder()
                .resource(this.resource1)
                .size(42)
                .build();

        var order = ResourceOrder.builder()
                .id(new UUID(42, 42))
                .orderDetails(List.of(orderDetail))
                .tribute(this.tribute)
                .build();

        doReturn(Stream.of(order).map(ResourceApprovedAndNotPaidResponse::new).collect(Collectors.toList()))
                .when(this.sponsorService)
                .getOrdersNotPaidAndApproved();

        this.get("/sponsor/tribute/" + this.tribute.getId() + "/payorder");
        this.page = PageFactory.initElements(driver, SponsorPayOrderPage.class);

        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver)driver);
        ngDriver.waitForAngularRequestsToFinish();

        return order.getId();
    }

    @Test
    public void payOrderRedirect() {
        final var orderId = prepareOrder();

        var orderRow = this.page.getOrderRows().get(0);

        orderRow.getPayButton().click();

        this.switchToNewWindow(this.sourceWindowHandle);

        assertThat(this.driver.getCurrentUrl(), endsWith("#/capitol/payment?id=" + orderId));
    }


    @Test
    public void payOrderApprove() {
        final var orderId = prepareOrder();

        var orderRow = this.page.getOrderRows().get(0);

        orderRow.getPayButton().click();

        this.approvePayment(this.sourceWindowHandle);

        verify(this.paymentManagerService, times(1)).approvePayment(new PaymentRequest(orderId));

        this.page = PageFactory.initElements(this.driver, SponsorPayOrderPage.class);

        Assertions.assertEquals("Оплата подтверждена", this.page.getPaymentStatus().getText());

        var sourceUrl = driver.getCurrentUrl();

        this.page.getGoBackButton().click();

        this.redirectWait(sourceUrl);

        assertThat(driver.getCurrentUrl(), endsWith("#/sponsor"));
    }

    @Test
    public void payOrderDeny() {
        final var orderId = prepareOrder();

        var orderRow = this.page.getOrderRows().get(0);

        orderRow.getPayButton().click();

        this.denyPayment(this.sourceWindowHandle);

        verify(this.paymentManagerService, times(0)).approvePayment(new PaymentRequest(orderId));

        this.page = PageFactory.initElements(this.driver, SponsorPayOrderPage.class);

        Assertions.assertEquals("Ошибка оплаты", this.page.getPaymentStatus().getText());
    }

    @Test
    public void payOrderDenyGoBack() {
        prepareOrder();

        var orderRow = this.page.getOrderRows().get(0);

        orderRow.getPayButton().click();

        this.denyPayment(this.sourceWindowHandle);

        this.page = PageFactory.initElements(this.driver, SponsorPayOrderPage.class);

        this.page.getGoBackButton().click();

        assertThat(driver.getCurrentUrl(), endsWith("#/sponsor"));
    }

    @Test
    public void payOrderDenyRetry() {
        final var orderId = prepareOrder();

        var orderRow = this.page.getOrderRows().get(0);

        orderRow.getPayButton().click();

        this.denyPayment(this.sourceWindowHandle);

        this.page = PageFactory.initElements(this.driver, SponsorPayOrderPage.class);

        this.page.getRetryButton().click();

        this.switchToNewWindow(this.sourceWindowHandle);

        assertThat(this.driver.getCurrentUrl(), endsWith("#/capitol/payment?id=" + orderId));
    }

    @Test
    public void payOrderDenyReselectResources() {
        prepareOrder();

        var orderRow = this.page.getOrderRows().get(0);

        orderRow.getPayButton().click();

        this.denyPayment(this.sourceWindowHandle);

        this.page = PageFactory.initElements(this.driver, SponsorPayOrderPage.class);

        this.page.getReselectResourcesButton().click();

        Assertions.assertTrue(this.page.getOrderRows().get(0).getPayButton().isDisplayed());
    }


    @Test
    public void payOrderDenyReselectTribute() {
        prepareOrder();

        var orderRow = this.page.getOrderRows().get(0);

        orderRow.getPayButton().click();

        this.denyPayment(this.sourceWindowHandle);

        this.page = PageFactory.initElements(this.driver, SponsorPayOrderPage.class);

        final var sourceUrl = driver.getCurrentUrl();

        this.page.getReselectTributeButton().click();

        this.redirectWait(sourceUrl);

        assertThat(driver.getCurrentUrl(), endsWith("#/sponsor/tributes"));
    }

    @Test
    public void payOrderIgnore() {
        final var orderId = prepareOrder();

        var orderRow = this.page.getOrderRows().get(0);

        orderRow.getPayButton().click();

        this.driver.switchTo().window(this.sourceWindowHandle);

        verify(this.paymentManagerService, times(0)).approvePayment(new PaymentRequest(orderId));

        this.page = PageFactory.initElements(this.driver, SponsorPayOrderPage.class);

        Assertions.assertEquals("Ожидаем подтверждения оплаты", this.page.getPaymentStatus().getText());
    }

    @Test
    public void payOrderIgnoreGoBack() {
        prepareOrder();

        var orderRow = this.page.getOrderRows().get(0);

        orderRow.getPayButton().click();

        this.driver.switchTo().window(this.sourceWindowHandle);

        this.page = PageFactory.initElements(this.driver, SponsorPayOrderPage.class);

        this.page.getGoBackButton().click();

        assertThat(driver.getCurrentUrl(), endsWith("#/sponsor"));
    }

    @Test
    public void payOrderIgnoreRetry() {
        final var orderId = prepareOrder();

        var orderRow = this.page.getOrderRows().get(0);

        orderRow.getPayButton().click();

        this.driver.switchTo().window(this.sourceWindowHandle);

        this.page = PageFactory.initElements(this.driver, SponsorPayOrderPage.class);

        var oldWindows = this.driver.getWindowHandles();

        this.page.getRetryButton().click();

        this.switchToNewWindow(oldWindows);

        assertThat(this.driver.getCurrentUrl(), endsWith("#/capitol/payment?id=" + orderId));
    }
}
