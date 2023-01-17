package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.order.OrderDetail;
import ru.itmo.hungergames.model.entity.order.Resource;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.MentorResponse;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.util.SecurityUtil;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
class MentorServiceImplTest {
    @Autowired
    MentorServiceImpl mentorService;
    @MockBean
    MentorRepository mentorRepository;
    @MockBean
    TributeRepository tributeRepository;
    @MockBean
    ResourceOrderRepository resourceOrderRepository;
    @MockBean
    SecurityUtil securityUtil;
    @MockBean
    OrderDetailRepository orderDetailRepository;
    @MockBean
    ResourceRepository resourceRepository;

    private final Mentor mentor1 = Mentor.builder()
            .id(new UUID(42, 42))
            .district(1)
            .name("name")
            .username("mentor1")
            .userRoles(Set.of(UserRole.MENTOR))
            .build();

    private final Mentor mentor2 = Mentor.builder()
            .id(new UUID(42, 43))
            .district(1)
            .name("name")
            .username("mentor2")
            .userRoles(Set.of(UserRole.MENTOR))
            .build();

    @Test
    void getAllMentors() {
        Mockito.when(mentorRepository.findAll()).thenReturn(List.of(mentor1, mentor2));

        List<MentorResponse> mentors = mentorService.getAllMentors();
        Mockito.verify(mentorRepository, Mockito.times(1)).findAll();

        assertTrue(mentors.containsAll(List.of(new MentorResponse(mentor1), new MentorResponse(mentor2))));
    }

    @Test
    void getOrdersForApproval() {
        Mockito.doReturn(mentor1.getId())
                .when(securityUtil)
                .getAuthenticatedUserId();

        Tribute tribute = Tribute.builder()
                .id(new UUID(42, 1))
                .mentor(mentor1)
                .username("tribute")
                .name("name")
                .build();

        Sponsor sponsor = Sponsor.builder()
                .id(new UUID(42, 1))
                .username("tribute")
                .name("name")
                .build();


        ResourceOrder order1 = ResourceOrder.builder()
                .id(new UUID(0, 42))
                .paid(true)
                .approved(null)
                .tribute(tribute)
                .sponsor(sponsor)
                .orderDetails(new ArrayList<>())
                .build();
        ResourceOrder order2 = ResourceOrder.builder()
                .id(new UUID(1, 42))
                .paid(true)
                .approved(null)
                .tribute(tribute)
                .sponsor(sponsor)
                .orderDetails(new ArrayList<>())
                .build();

        Mockito.when(resourceOrderRepository.findAllByTribute_MentorIdAndPaidAndApproved(mentor1.getId(), true, null)).thenReturn(List.of(order1, order2));

        List<ResourceApprovalResponse> ordersForApproval = mentorService.getOrdersForApproval();

        assertTrue(ordersForApproval.containsAll(List.of(new ResourceApprovalResponse(order1), new ResourceApprovalResponse(order2))));

        Mockito.verify(securityUtil, Mockito.times(1)).getAuthenticatedUserId();
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).findAllByTribute_MentorIdAndPaidAndApproved(mentor1.getId(), true, null);
    }

    @Test
    void approveResourcesToSend() {
        Mockito.doReturn(mentor1.getId())
                .when(securityUtil)
                .getAuthenticatedUserId();
        Mockito.doReturn(Optional.of(mentor1))
                .when(mentorRepository)
                .findById(mentor1.getId());

        UUID orderId = new UUID(1, 1);
        ResourceOrder order = ResourceOrder.builder()
                .id(orderId)
                .paid(true)
                .build();

        Mockito.doReturn(Optional.of(order))
                .when(resourceOrderRepository)
                .findByIdAndTribute_MentorIdAndPaid(orderId, mentor1.getId(), true);
        Mockito.doReturn(order)
                .when(resourceOrderRepository)
                .save(ArgumentMatchers.any(ResourceOrder.class));

        mentorService.approveResourcesToSend(new ApproveResourcesRequest(orderId, true));

        Mockito.verify(securityUtil, Mockito.times(1)).getAuthenticatedUserId();
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).save(order);
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).findByIdAndTribute_MentorIdAndPaid(orderId, mentor1.getId(), true);

        assertTrue(order.getApproved());

        Mockito.clearInvocations(resourceOrderRepository);
        Mockito.clearInvocations(securityUtil);

        mentorService.approveResourcesToSend(new ApproveResourcesRequest(orderId, false));

        Mockito.verify(resourceOrderRepository, Mockito.times(1)).save(order);
        Mockito.clearInvocations(resourceOrderRepository);

        assertFalse(order.getApproved());

        Mockito.verify(securityUtil, Mockito.times(1)).getAuthenticatedUserId();
    }

    @Test
    void sendResourcesToSponsor() {
        UUID tributeId = new UUID(42, 1);
        Tribute tribute = new Tribute();
        tribute.setId(tributeId);
        Mockito.when(tributeRepository.findById(tributeId))
                .thenReturn(Optional.of(tribute));


        Mockito.doReturn(Optional.of(mentor1))
                .when(mentorRepository)
                .findById(mentor1.getId());

        Resource resource1 = Resource.builder()
                .id(new UUID(42, 42))
                .name("1")
                .price(BigDecimal.valueOf(100))
                .build();
        Resource resource2 = Resource.builder()
                .id(new UUID(42, 43))
                .name("1")
                .price(BigDecimal.valueOf(200))
                .build();

        Mockito.doReturn(Optional.of(resource1)).when(resourceRepository).findById(resource1.getId());
        Mockito.doReturn(Optional.of(resource2)).when(resourceRepository).findById(resource2.getId());


        OrderDetail orderDetail1 = OrderDetail.builder()
                .resource(resource1)
                .size(1)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .resource(resource2)
                .size(2)
                .build();

        Mockito.doReturn(orderDetail1).when(orderDetailRepository).save(orderDetail1);
        Mockito.doReturn(orderDetail2).when(orderDetailRepository).save(orderDetail2);

        List<OrderDetailRequest> orderDetailRequestList = new ArrayList<>();
        orderDetailRequestList.add(new OrderDetailRequest(new UUID(42, 42), 1));
        orderDetailRequestList.add(new OrderDetailRequest(new UUID(42, 43), 2));

        ResourceOrderRequest resourceOrderRequest = new ResourceOrderRequest(tributeId, orderDetailRequestList);

        BigDecimal expectedPrice = BigDecimal.valueOf(500);
        UUID expectedUUID = new UUID(24, 24);
        ResourceOrder expectedOrder = ResourceOrder.builder()
                .orderDetails(List.of(orderDetail1, orderDetail2))
                .tribute(tribute)
                .price(expectedPrice)
                .approved(true)
                .build();
        ResourceOrder savedExpectedOrder = ResourceOrder.builder()
                .id(expectedUUID)
                .orderDetails(List.of(orderDetail1, orderDetail2))
                .tribute(tribute)
                .price(expectedPrice)
                .approved(true)
                .build();
        Mockito.doReturn(savedExpectedOrder).when(resourceOrderRepository).save(expectedOrder);

        ResourceOrderResponse resourceOrderResponse = mentorService.sendResourcesToSponsor(resourceOrderRequest);

        Mockito.verify(resourceOrderRepository, Mockito.times(1)).save(expectedOrder);

        assertEquals(expectedPrice, resourceOrderResponse.getPrice());
        assertEquals(expectedUUID, resourceOrderResponse.getOrderId());
    }

    @Test
    void getAllTributes() {
        UUID mentorID = mentor1.getId();
        Mockito.doReturn(mentorID)
                .when(securityUtil)
                .getAuthenticatedUserId();

        Tribute tribute1 = Tribute.builder()
                .id(new UUID(42, 24))
                .mentor(mentor1)
                .name("name")
                .username("tribute1")
                .userRoles(Set.of(UserRole.TRIBUTE))
                .build();
        Tribute tribute2 = Tribute.builder()
                .id(new UUID(42, 25))
                .mentor(mentor1)
                .name("name")
                .username("tribute2")
                .userRoles(Set.of(UserRole.TRIBUTE))
                .build();


        Mockito.doReturn(List.of(tribute1, tribute2)).when(tributeRepository).findAllByMentor_Id(mentorID);

        List<TributeResponse> tributeResponses = mentorService.getAllTributes();

        assertTrue(tributeResponses.containsAll(List.of(new TributeResponse(tribute1), new TributeResponse(tribute2))));
    }

    @Test
    void getMentorById() {
        Mentor mentor = new Mentor();
        UUID id = new UUID(42, 42);
        mentor.setName("name");
        mentor.setId(id);
        Mockito.doReturn(Optional.of(mentor))
                .when(mentorRepository)
                .findById(id);

        MentorResponse mentorResponse = mentorService.getMentorById(id);
        assertEquals("name", mentorResponse.getName());
        assertEquals(id, mentorResponse.getId());
        Mockito.verify(mentorRepository, Mockito.times(1)).findById(id);
    }
}