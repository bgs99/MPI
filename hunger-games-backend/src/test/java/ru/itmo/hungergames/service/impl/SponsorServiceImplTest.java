package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.*;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;
import ru.itmo.hungergames.model.response.SponsorResponse;
import ru.itmo.hungergames.repository.OrderDetailRepository;
import ru.itmo.hungergames.repository.ResourceOrderRepository;
import ru.itmo.hungergames.repository.SponsorRepository;
import ru.itmo.hungergames.repository.TributeRepository;
import ru.itmo.hungergames.service.SponsorService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class SponsorServiceImplTest {
    @Autowired
    SponsorService sponsorService;
    @MockBean
    SponsorRepository sponsorRepository;
    @MockBean
    TributeRepository tributeRepository;
    @MockBean
    SecurityUtil securityUtil;
    @Autowired
    ResourceOrderRepository resourceOrderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Test
    void getAllSponsors() {
        sponsorService.getAllSponsors();
        Mockito.verify(sponsorRepository, Mockito.times(1)).findAll();
    }

    @Test
    @Sql(value = {"/initScripts/create-sponsor.sql", "/initScripts/create-tribute.sql", "/initScripts/create-resources.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-sponsor.sql", "/initScripts/drop-tribute.sql", "/initScripts/drop-resources.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendResourcesForApproval() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Tribute tribute = new Tribute();
        tribute.setId(tributeId);
        Mockito.doReturn(Optional.of(tribute))
                .when(tributeRepository)
                .findById(tributeId);
        UUID sponsorId = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");

        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);
        Mockito.doReturn(Optional.of(sponsor))
                .when(sponsorRepository)
                .findById(sponsorId);
        Mockito.doReturn(sponsor)
                .when(securityUtil)
                .getAuthenticatedUser();

        List<OrderDetailRequest> orderDetailRequestList = new ArrayList<>();
        orderDetailRequestList.add(new OrderDetailRequest(UUID.fromString("33ff5ee9-c0d7-4955-b2cd-a0aa3d484b98"), 1));
        orderDetailRequestList.add(new OrderDetailRequest(UUID.fromString("47f75e81-4f14-4af5-bce2-b6d5af372d94"), 2));

        ResourceOrderRequest resourceOrderRequest = new ResourceOrderRequest(tributeId, orderDetailRequestList);
        ResourceOrderResponse resourceOrderResponse = sponsorService.sendResourcesForApproval(resourceOrderRequest);
        ResourceOrder order = resourceOrderRepository.findById(resourceOrderResponse.getOrderId()).get();

        List<OrderDetail> orderDetails = order.getOrderDetails();
        assertEquals(sponsorId, order.getSponsor().getId());
        assertEquals(tributeId, order.getTribute().getId());
        assertNotNull(order.getPrice());
        assertFalse(order.isPaid());
        assertNull(order.getApproved());
        assertEquals(2, orderDetails.size());

        orderDetailRepository.deleteAll(orderDetails);
        order.setOrderDetails(new ArrayList<>());
        resourceOrderRepository.delete(order);
    }
    @Test
    @Sql(value = {"/initScripts/create-sponsor.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-sponsor.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendResourcesTributeNotExists(){
        UUID sponsorId = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);
        Mockito.doReturn(Optional.of(sponsor))
                .when(sponsorRepository)
                .findById(sponsorId);
        Mockito.doReturn(sponsor)
                .when(securityUtil)
                .getAuthenticatedUser();

        List<OrderDetailRequest> orderDetailRequestList = new ArrayList<>();
        UUID tributeId = UUID.randomUUID();

        ResourceOrderRequest resourceOrderRequest = new ResourceOrderRequest(tributeId, orderDetailRequestList);
        Throwable thrown = catchThrowable(()->sponsorService.sendResourcesForApproval(resourceOrderRequest));
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).contains("There's no tribute with the ID");
    }

    @Test
    void getOrdersNotPaidAndApproved() {
        sponsorService.getOrdersNotPaidAndApproved();
        //Mockito.verify(resourceOrderRepository, Mockito.times(1)).findAllByPaidAndApproved(false, true);
    }

    @Test
    void getSponsorById() {
        Sponsor sponsor = new Sponsor();
        UUID id = UUID.randomUUID();
        sponsor.setName("name");
        sponsor.setId(id);
        Mockito.doReturn(Optional.of(sponsor))
                .when(sponsorRepository)
                .findById(id);

        SponsorResponse sponsorById = sponsorService.getSponsorById(id);
        assertEquals("name", sponsorById.getName());
        assertEquals(id, sponsorById.getId());
        Mockito.verify(sponsorRepository, Mockito.times(1)).findById(id);
    }
}