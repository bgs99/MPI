package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.Mentor;
import ru.itmo.hungergames.model.entity.Orders;
import ru.itmo.hungergames.model.entity.ResourceOrder;
import ru.itmo.hungergames.model.entity.Tribute;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.response.MentorResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.repository.MentorRepository;
import ru.itmo.hungergames.repository.OrdersRepository;
import ru.itmo.hungergames.repository.ResourceOrderRepository;
import ru.itmo.hungergames.repository.TributeRepository;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
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
    @Test
    void getAllMentors() {
        mentorService.getAllMentors();
        Mockito.verify(mentorRepository, Mockito.times(1)).findAll();
    }

    @Test
    @Sql(value = {"/initScripts/create-mentor.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-mentor.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).findAllByTribute_MentorIdAndPaidAndApproved(mentorID,true, null);
    }

    @Test
    @Sql(value = {"/initScripts/create-mentor.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-mentor.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
        UUID orderId = UUID.randomUUID();
        ResourceOrder order = ResourceOrder.builder().id(orderId).paid(true).build();
        Mockito.doReturn(Optional.of(order)).when(resourceOrderRepository).findByIdAndTribute_MentorIdAndPaid(orderId, mentorID, true);
        mentorService.approveResourcesToSend(new ApproveResourcesRequest(orderId, true));
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).save(order);
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).findByIdAndTribute_MentorIdAndPaid(orderId, mentorID, true);
        assertTrue(order.getApproved());
        mentorService.approveResourcesToSend(new ApproveResourcesRequest(orderId, false));
        assertFalse(order.getApproved());
    }

    @Test
    void sendResourcesToSponsor() {

    }

    @Test
    @Sql(value = {"/initScripts/create-mentor.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-mentor.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllTributes() {
        UUID mentorID = UUID.fromString("1d3ad419-e98f-43f1-9ac6-08776036cded");
        Mockito.doReturn(mentorID)
                .when(securityUtil)
                .getAuthenticatedUserId();
        mentorService.getAllTributes();
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