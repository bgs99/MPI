package ru.itmo.hungergames.selenium.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.order.Resource;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.selenium.pages.MentorSuggestOrdersPage;
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

import static org.mockito.Mockito.*;

@SeleniumTest
public class MentorSuggestOrdersPageTests extends SeleniumTestBase {
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

    private MentorSuggestOrdersPage page;

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

    private final List<Resource> resources = List.of(resource1, resource2, resource3);

    private List<MentorSuggestOrdersPage.ResourceRow> resourceRows;

    @BeforeEach
    public void setUp() {
        this.authenticate(this.mentor);

        doReturn(new TributeResponse(this.tribute)).when(this.tributeService).getTributeById(this.tribute.getId());
        doReturn(resources).when(this.resourceService).getAllResources();

        this.page = this.getInit("/mentor/tribute/" + this.tribute.getId() + "/resources", MentorSuggestOrdersPage.class);

        this.page.clear();

        this.resourceRows = this.page.getResourceRows();
    }

    @Test
    public void correctRecipient() {
        Assertions.assertEquals(this.tribute.getName(), this.page.getRecipient());
    }

    @Test
    public void listResources() {
        final var expectedResourceNames = this.resources.stream().map(Resource::getName).collect(Collectors.toList());
        final var actualResourceNames = this.resourceRows.stream().map(MentorSuggestOrdersPage.ResourceRow::getName).collect(Collectors.toList());

        Assertions.assertEquals(expectedResourceNames, actualResourceNames);
    }

    @Test
    public void orderResources() {
        final var expectedRequest = new ResourceOrderRequest(this.tribute.getId(), List.of(
                new OrderDetailRequest(this.resource2.getId(), 42),
                new OrderDetailRequest(this.resource3.getId(), 43)
        ));

        doReturn(null).when(this.mentorService).sendResourcesToSponsor(expectedRequest);

        this.resourceRows.get(1).getAmountInput().sendKeys("42");
        this.resourceRows.get(2).getAmountInput().sendKeys("43");

        this.page.orderSelected();

        this.waitForAngularRequests();

        verify(this.mentorService).sendResourcesToSponsor(expectedRequest);
    }
}
