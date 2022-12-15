package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.order.OrderDetail;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.MentorResponse;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.repository.MentorRepository;
import ru.itmo.hungergames.repository.OrderDetailRepository;
import ru.itmo.hungergames.repository.ResourceOrderRepository;
import ru.itmo.hungergames.repository.TributeRepository;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MentorServiceImplTest {
    @Autowired
    MentorServiceImpl mentorService;
    @SpyBean
    MentorRepository mentorRepository;
    @SpyBean
    TributeRepository tributeRepository;
    @SpyBean
    ResourceOrderRepository resourceOrderRepository;
    @MockBean
    SecurityUtil securityUtil;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Test
    @Sql(value = {"/initScripts/create-mentor.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-mentor.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllMentors() {
        mentorService.getAllMentors();
        Mockito.verify(mentorRepository, Mockito.times(1)).findAll();
    }

    @Test
    @Sql(value = {"/initScripts/create-mentor.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-mentor.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getOrdersForApproval() {
        UUID mentorID = UUID.fromString("1d3ad419-e98f-43f1-9ac6-08776036cded");
        Mentor mentor = new Mentor();
        mentor.setId(mentorID);

        Mockito.doReturn(Optional.of(mentor))
                .when(mentorRepository)
                .findById(mentorID);
        Mockito.doReturn(mentor)
                .when(securityUtil)
                .getAuthenticatedUser();

        mentorService.getOrdersForApproval();

        Mockito.verify(resourceOrderRepository, Mockito.times(1)).findAllByTribute_MentorIdAndPaidAndApproved(mentorID, true, null);
    }

    @Test
    @Sql(value = {"/initScripts/create-mentor.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-mentor.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void approveResourcesToSend() {
        UUID mentorID = UUID.fromString("1d3ad419-e98f-43f1-9ac6-08776036cded");

        Mentor mentor = new Mentor();
        mentor.setId(mentorID);

        Mockito.doReturn(Optional.of(mentor))
                .when(mentorRepository)
                .findById(mentorID);
        Mockito.doReturn(mentor)
                .when(securityUtil)
                .getAuthenticatedUser();

        UUID orderId = new UUID(1, 1);
        ResourceOrder order = ResourceOrder.builder().id(orderId).paid(true).build();
        Mockito.doReturn(Optional.of(order)).when(resourceOrderRepository).findByIdAndTribute_MentorIdAndPaid(orderId, mentorID, true);

        mentorService.approveResourcesToSend(new ApproveResourcesRequest(orderId, true));
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).save(order);
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).findByIdAndTribute_MentorIdAndPaid(orderId, mentorID, true);
        assertTrue(order.getApproved());
        Mockito.clearInvocations(resourceOrderRepository);

        mentorService.approveResourcesToSend(new ApproveResourcesRequest(orderId, false));
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).save(order);
        Mockito.clearInvocations(resourceOrderRepository);
        assertFalse(order.getApproved());
    }

    @Test
    @DirtiesContext
    @Sql(value = {"/initScripts/create-mentor.sql", "/initScripts/create-resources.sql", "/initScripts/create-tribute.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-mentor.sql", "/initScripts/drop-resources.sql", "/initScripts/drop-tribute.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendResourcesToSponsor() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Tribute tribute = new Tribute();
        tribute.setId(tributeId);
        Mockito.when(tributeRepository.findById(tributeId))
                .thenReturn(Optional.of(tribute));

        UUID mentorID = UUID.fromString("1d3ad419-e98f-43f1-9ac6-08776036cded");
        Mentor mentor = new Mentor();
        mentor.setId(mentorID);

        Mockito.doReturn(Optional.of(mentor))
                .when(mentorRepository)
                .findById(mentorID);
        Mockito.doReturn(mentor)
                .when(securityUtil)
                .getAuthenticatedUser();

        List<OrderDetailRequest> orderDetailRequestList = new ArrayList<>();
        orderDetailRequestList.add(new OrderDetailRequest(UUID.fromString("33ff5ee9-c0d7-4955-b2cd-a0aa3d484b98"), 1));
        orderDetailRequestList.add(new OrderDetailRequest(UUID.fromString("47f75e81-4f14-4af5-bce2-b6d5af372d94"), 2));

        ResourceOrderRequest resourceOrderRequest = new ResourceOrderRequest(tributeId, orderDetailRequestList);
        ResourceOrderResponse resourceOrderResponse = mentorService.sendResourcesToSponsor(resourceOrderRequest);
        ResourceOrder order = resourceOrderRepository.findById(resourceOrderResponse.getOrderId()).orElseThrow();
        List<OrderDetail> orderDetails = order.getOrderDetails();

        Mockito.verify(resourceOrderRepository, Mockito.times(1)).save(ArgumentMatchers.any(ResourceOrder.class));
        Mockito.clearInvocations(resourceOrderRepository);

        assertNull(order.getSponsor());
        assertEquals(tributeId, order.getTribute().getId());
        assertNotNull(order.getPrice());
        assertFalse(order.isPaid());
        assertTrue(order.getApproved());
        assertEquals(2, orderDetails.size());

        orderDetailRepository.deleteAll(orderDetails);
        order.setOrderDetails(new ArrayList<>());
        resourceOrderRepository.delete(order);
    }

    @Test
    @Sql(value = {"/initScripts/create-mentor-with-tributes.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-mentor-with-tributes.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllTributes() {
        UUID mentorID = UUID.fromString("1d3ad419-e98f-43f1-9ac6-08776036cded");
        Mockito.doReturn(mentorID)
                .when(securityUtil)
                .getAuthenticatedUserId();
        List<UUID> initialUUIDs = new ArrayList<>();
        initialUUIDs.add(UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32"));
        initialUUIDs.add(UUID.fromString("c0b91cca-27ba-49d2-85e1-290cbd73d45e"));
        initialUUIDs.add(UUID.fromString("3ee25464-bdec-4237-a335-94c1f376e8d6"));

        List<TributeResponse> tributeResponses = mentorService.getAllTributes();
        List<UUID> uuids = tributeResponses.stream().map(TributeResponse::getId).toList();

        assertTrue(uuids.containsAll(initialUUIDs));
        Mockito.verify(tributeRepository, Mockito.times(1)).findAllByMentor_Id(mentorID);
    }

    @Test
    void getMentorById() {
        Mentor mentor = new Mentor();
        UUID id = UUID.randomUUID();
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