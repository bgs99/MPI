package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.exception.NotNewsSubscribedException;
import ru.itmo.hungergames.model.entity.order.NewsSubscriptionOrder;
import ru.itmo.hungergames.model.entity.order.OrderDetail;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.request.NewsSubscriptionOrderRequest;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.*;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.service.SponsorService;
import ru.itmo.hungergames.util.SecurityUtil;
import ru.itmo.hungergames.util.SponsorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SponsorServiceImplTest {
    @Autowired
    SponsorService sponsorService;
    @SpyBean
    SponsorRepository sponsorRepository;
    @MockBean
    TributeRepository tributeRepository;
    @MockBean
    SecurityUtil securityUtil;
    @SpyBean
    ResourceOrderRepository resourceOrderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @SpyBean
    SponsorUtil sponsorUtil;
    @SpyBean
    NewsSubscriptionOrderRepository newsSubscriptionOrderRepository;
    @SpyBean
    NewsRepository newsRepository;

    @Test
    @Sql(value = {"/initScripts/create-multiple-sponsors.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-multiple-sponsors.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllSponsors() {
        List<UUID> initialUUIDs = new ArrayList<>();
        initialUUIDs.add(UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460"));
        initialUUIDs.add(UUID.fromString("e37ec309-5b49-4985-97c0-e12451dc177e"));
        initialUUIDs.add(UUID.fromString("1ebfbc1c-fde8-40fa-8572-488738c16a9b"));

        List<SponsorResponse> sponsorResponses = sponsorService.getAllSponsors();
        List<UUID> uuids = sponsorResponses.stream().map(SponsorResponse::getId).toList();

        assertTrue(uuids.containsAll(initialUUIDs));
        Mockito.verify(sponsorRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DirtiesContext
    @Sql(value = {"/initScripts/create-sponsor.sql", "/initScripts/create-tribute.sql", "/initScripts/create-resources.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-sponsor.sql", "/initScripts/drop-tribute.sql", "/initScripts/drop-resources.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendResourcesForApproval() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Tribute tribute = new Tribute();
        tribute.setId(tributeId);
        Mockito.when(tributeRepository.findById(tributeId))
                .thenReturn(Optional.of(tribute));
        UUID sponsorId = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);
        Mockito.when(sponsorRepository.findById(sponsorId))
                .thenReturn(Optional.of(sponsor));
        Mockito.when(securityUtil.getAuthenticatedUserId())
                .thenReturn(sponsorId);

        List<OrderDetailRequest> orderDetailRequestList = new ArrayList<>();
        orderDetailRequestList.add(new OrderDetailRequest(UUID.fromString("33ff5ee9-c0d7-4955-b2cd-a0aa3d484b98"), 1));
        orderDetailRequestList.add(new OrderDetailRequest(UUID.fromString("47f75e81-4f14-4af5-bce2-b6d5af372d94"), 2));

        ResourceOrderRequest resourceOrderRequest = new ResourceOrderRequest(tributeId, orderDetailRequestList);
        ResourceOrderResponse resourceOrderResponse = sponsorService.sendResourcesForApproval(resourceOrderRequest);
        ResourceOrder order = resourceOrderRepository.findById(resourceOrderResponse.getOrderId()).orElseThrow();
        List<OrderDetail> orderDetails = order.getOrderDetails();

        Mockito.verify(resourceOrderRepository, Mockito.times(1)).save(ArgumentMatchers.any(ResourceOrder.class));
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
    @DirtiesContext
    @Sql(value = {"/initScripts/create-sponsor.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-sponsor.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendResourcesTributeNotExists() {
        UUID sponsorId = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);
        Mockito.when(sponsorRepository.findById(sponsorId))
                .thenReturn(Optional.of(sponsor));
        Mockito.when(securityUtil.getAuthenticatedUser())
                .thenReturn(sponsor);

        List<OrderDetailRequest> orderDetailRequestList = new ArrayList<>();
        UUID tributeId = new UUID(1, 1);

        ResourceOrderRequest resourceOrderRequest = new ResourceOrderRequest(tributeId, orderDetailRequestList);
        Throwable thrown = catchThrowable(() -> sponsorService.sendResourcesForApproval(resourceOrderRequest));

        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).contains("There's no tribute with the ID");
    }

    @Test
    @Sql(value = {"/initScripts/create-orders-not-paid-and-approved.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-orders-not-paid-and-approved.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getOrdersNotPaidAndApproved() {
        List<UUID> initialUUIDs = new ArrayList<>();
        initialUUIDs.add(UUID.fromString("3d6e3de8-3311-4d89-9c92-4b5bf13f55c7"));
        initialUUIDs.add(UUID.fromString("61091dd1-84db-44ae-a7fe-4d98316a63cc"));


        List<ResourceApprovedAndNotPaidResponse> ordersNotPaidAndApproved = sponsorService.getOrdersNotPaidAndApproved();
        List<UUID> uuids = ordersNotPaidAndApproved.stream().map(ResourceApprovedAndNotPaidResponse::getOrderId).toList();

        assertTrue(uuids.containsAll(initialUUIDs));
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).findAllByPaidAndApproved(false, true);
    }

    @Test
    @Sql(value = {"/initScripts/create-sponsor.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-sponsor.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getSponsorById() {
        UUID id = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        SponsorResponse sponsorById = sponsorService.getSponsorById(id);

        assertEquals(id, sponsorById.getId());
        assertEquals("sponsor-test", sponsorById.getName());
        assertEquals("sponsor-name", sponsorById.getUsername());
        Mockito.verify(sponsorRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void getSponsorByIdNotExists() {
        UUID id = new UUID(1, 1);
        Throwable thrown = catchThrowable(() -> sponsorService.getSponsorById(id));

        Mockito.verify(sponsorRepository, Mockito.times(1)).findById(id);
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).contains(String.format("Sponsor with id=%s doesn't exist", id));
    }

    @Test
    @DirtiesContext
    @Sql(value = {"/initScripts/create-sponsor.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-sponsor.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void subscribeToNews() {
        String email = "example@email.com";
        UUID sponsorId = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        Mockito.when(securityUtil.getAuthenticatedUserId())
                .thenReturn(sponsorId);

        NewsSubscriptionOrderRequest newsSubscriptionOrderRequest = new NewsSubscriptionOrderRequest(email);
        NewsSubscriptionOrderResponse newsSubscriptionOrderResponse = sponsorService.subscribeToNews(newsSubscriptionOrderRequest);

        Sponsor sponsor = sponsorRepository.findById(sponsorId).orElseThrow();
        NewsSubscriptionOrder newsSubscriptionOrder = sponsor.getNewsSubscriptionOrder();

        Mockito.verify(sponsorUtil, Mockito.times(1))
                .checkIfAlreadySubscribed(ArgumentMatchers.any(Sponsor.class));
        Mockito.verify(newsSubscriptionOrderRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(NewsSubscriptionOrder.class));
        Mockito.verify(sponsorRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(Sponsor.class));


        assertEquals(newsSubscriptionOrderResponse.getOrderId(), newsSubscriptionOrder.getId());
        assertEquals(sponsorId, newsSubscriptionOrder.getSponsor().getId());
        assertEquals(email, newsSubscriptionOrder.getEmail());

        sponsor.setNewsSubscriptionOrder(null);
        sponsorRepository.save(sponsor);
        newsSubscriptionOrderRepository.delete(newsSubscriptionOrder);
    }

    @Test
    @Sql(value = {"/initScripts/create-sponsor.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-sponsor.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getNewsWithoutSubscription() {
        UUID sponsorId = UUID.fromString("4a9f1d37-c6fd-4391-8082-655bb98fb460");
        Mockito.when(securityUtil.getAuthenticatedUserId())
                .thenReturn(sponsorId);

        Throwable thrown = catchThrowable(() -> sponsorService.getNews());
        assertThat(thrown).isInstanceOf(NotNewsSubscribedException.class);
        assertThat(thrown.getMessage()).contains("You have no access to news. Buy subscription");
    }

    @Test
    @Sql(value = {"/initScripts/create-sponsor-with-subscription.sql", "/initScripts/create-moderators-and-news.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-sponsor-with-subscription.sql", "/initScripts/drop-moderators-and-news.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getNews() {
        UUID sponsorId = UUID.fromString("523adea8-9f98-41a3-bae0-1e4875aceaae");
        int expectedSize = 4;
        Mockito.when(securityUtil.getAuthenticatedUserId())
                .thenReturn(sponsorId);
        List<NewsResponse> news = sponsorService.getNews();

        Mockito.verify(sponsorUtil, Mockito.times(1)).checkIfSponsorCanSeeNews(ArgumentMatchers.any(Sponsor.class));
        Mockito.verify(newsRepository, Mockito.times(1)).findAll();

        assertTrue(news.size() >= expectedSize);
    }
}