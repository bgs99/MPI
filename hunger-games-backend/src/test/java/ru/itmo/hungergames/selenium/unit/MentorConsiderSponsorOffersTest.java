package ru.itmo.hungergames.selenium.unit;

import com.paulhammant.ngwebdriver.NgWebDriver;
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
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.selenium.pages.MentorConsiderSponsorOffersPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.MentorService;
import ru.itmo.hungergames.service.ResourceService;
import ru.itmo.hungergames.service.TributeService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SeleniumTest
public class MentorConsiderSponsorOffersTest extends SeleniumTestBase {
    @MockBean
    private TributeService tributeService;
    @MockBean
    private ResourceService resourceService;
    @MockBean
    private MentorService mentorService;

    private final Mentor mentor = Mentor.builder()
            .id(new UUID(42, 42))
            .username("mentor")
            .name("mentor")
            .userRoles(Set.of(UserRole.MENTOR))
            .build();

    private final Tribute tribute = Tribute.builder()
            .id(new UUID(42, 42))
            .name("test")
            .mentor(mentor)
            .build();

    private final Sponsor sponsor = Sponsor.builder()
            .id(new UUID(42, 42))
            .username("sponsor")
            .name("sponsor")
            .build();

    private MentorConsiderSponsorOffersPage page;

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
    private final List<Integer> orderSize = List.of(10, 42, 43);

    private final ResourceOrder resourceOrder1 = ResourceOrder.builder()
            .orderDetails(List.of(
                    new OrderDetail(new UUID(42, 2), this.orderSize.get(1), this.resource2),
                    new OrderDetail(new UUID(42, 2), this.orderSize.get(2), this.resource3)
            ))
            .sponsor(sponsor)
            .tribute(tribute)
            .build();
    private final ResourceOrder resourceOrder2 = ResourceOrder.builder()
            .orderDetails(List.of(
                    new OrderDetail(new UUID(42, 2), this.orderSize.get(0), this.resource1),
                    new OrderDetail(new UUID(42, 2), this.orderSize.get(2), this.resource3)
            ))
            .sponsor(sponsor)
            .tribute(tribute)
            .build();
    private final List<ResourceOrder> resourceOrders = List.of(resourceOrder1, resourceOrder2);
    private List<MentorConsiderSponsorOffersPage.OrderRow> orderRows;

    @BeforeEach
    public void setUp() {
        this.authenticate(this.mentor);
        this.page = new MentorConsiderSponsorOffersPage(driver);

        doReturn(new TributeResponse(this.tribute)).when(this.tributeService).getTributeById(this.tribute.getId());
        doReturn(List.of(resource1, resource2, resource3)).when(this.resourceService).getAllResources();
        doReturn(Stream.of(resourceOrder1, resourceOrder2)
                .map(ResourceApprovalResponse::new)
                .collect(Collectors.toList())).when(this.mentorService).getOrdersForApproval();

        this.get("/mentor/approval");
        PageFactory.initElements(driver, this.page);

        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver)driver);
        ngDriver.waitForAngularRequestsToFinish();

        this.orderRows = this.page.getOrderRows();
    }

    @Test
    public void listRequestsTest() {
        var expectedResourceOrdersSponsorName = this.resourceOrders.stream().map(r -> r.getSponsor().getName()).toList();
        var actualResourceOrdersSponsorName = this.orderRows.stream().map(MentorConsiderSponsorOffersPage.OrderRow::getSponsorName).toList();
        Assertions.assertEquals(expectedResourceOrdersSponsorName, actualResourceOrdersSponsorName);

        var expectedResourceOrdersTributeName = this.resourceOrders.stream().map(r -> r.getTribute().getName()).toList();
        var actualResourceOrdersTributeName = this.orderRows.stream().map(MentorConsiderSponsorOffersPage.OrderRow::getTributeName).toList();
        Assertions.assertEquals(expectedResourceOrdersTributeName, actualResourceOrdersTributeName);

        var expectedResourceOrdersResources = this.resourceOrders.stream().map(r -> r.getOrderDetails().toString()).toList();
        var actualResourceOrdersResources = this.orderRows.stream().map(MentorConsiderSponsorOffersPage.OrderRow::getResources).toList();
        Assertions.assertEquals(expectedResourceOrdersResources, actualResourceOrdersResources);
    }

    @Test
    public void approveRequestTest() {
        final var expectedRequest = new ApproveResourcesRequest(this.resourceOrder1.getId(), true);
        doNothing().when(this.mentorService).approveResourcesToSend(expectedRequest);

        this.orderRows.get(0).getApproveButton().click();

        this.waitForAngularRequests();
        verify(this.mentorService).approveResourcesToSend(expectedRequest);
    }


    @Test
    public void denyRequestTest() {
        final var expectedRequest = new ApproveResourcesRequest(this.resourceOrder1.getId(), false);
        doNothing().when(this.mentorService).approveResourcesToSend(expectedRequest);

        this.orderRows.get(0).getDenyButton().click();

        this.waitForAngularRequests();
        verify(this.mentorService).approveResourcesToSend(expectedRequest);
    }
}
