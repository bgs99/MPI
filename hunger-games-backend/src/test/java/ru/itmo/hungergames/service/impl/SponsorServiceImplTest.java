package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.exception.NotNewsSubscribedException;
import ru.itmo.hungergames.model.entity.News;
import ru.itmo.hungergames.model.entity.order.NewsSubscriptionOrder;
import ru.itmo.hungergames.model.entity.order.OrderDetail;
import ru.itmo.hungergames.model.entity.order.Resource;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.NewsSubscriptionOrderRequest;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.NewsResponse;
import ru.itmo.hungergames.model.response.ResourceApprovedAndNotPaidResponse;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;
import ru.itmo.hungergames.model.response.SponsorResponse;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.util.ApplicationParameters;
import ru.itmo.hungergames.util.SecurityUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
class SponsorServiceImplTest {
    @Autowired
    SponsorServiceImpl sponsorService;
    @MockBean
    SponsorRepository sponsorRepository;
    @MockBean
    TributeRepository tributeRepository;
    @MockBean
    SecurityUtil securityUtil;
    @MockBean
    ResourceOrderRepository resourceOrderRepository;
    @MockBean
    OrderDetailRepository orderDetailRepository;
    @MockBean
    NewsSubscriptionOrderRepository newsSubscriptionOrderRepository;
    @MockBean
    NewsRepository newsRepository;
    @MockBean
    ResourceRepository resourceRepository;

    @Test
    void getAllSponsors() {
        Sponsor sponsor1 = Sponsor.builder()
                .id(new UUID(42, 42))
                .name("name")
                .username("username1")
                .userRoles(Set.of(UserRole.SPONSOR))
                .build();
        Sponsor sponsor2 = Sponsor.builder()
                .id(new UUID(42, 43))
                .name("name")
                .username("username2")
                .userRoles(Set.of(UserRole.SPONSOR))
                .build();

        Mockito.doReturn(List.of(sponsor1, sponsor2)).when(sponsorRepository).findAll();

        var exceptedSponsors = List.of(new SponsorResponse(sponsor1), new SponsorResponse(sponsor2));

        assertEquals(exceptedSponsors, sponsorService.getAllSponsors());
    }

    @Test
    void sendResourcesForApproval() {
        UUID tributeId = new UUID(42, 42);
        Tribute tribute = new Tribute();
        tribute.setId(tributeId);
        Mockito.when(tributeRepository.findById(tributeId))
                .thenReturn(Optional.of(tribute));
        UUID sponsorId = new UUID(42, 43);
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);

        Mockito.when(sponsorRepository.findById(sponsorId))
                .thenReturn(Optional.of(sponsor));
        Mockito.when(securityUtil.getAuthenticatedUserId())
                .thenReturn(sponsorId);

        Resource resource1 = Resource.builder()
                .id(new UUID(42, 42))
                .name("1")
                .price(BigDecimal.valueOf(100))
                .build();
        Resource resource2 = Resource.builder()
                .id(new UUID(42, 43))
                .name("2")
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
                .sponsor(sponsor)
                .build();
        ResourceOrder savedExpectedOrder = ResourceOrder.builder()
                .id(expectedUUID)
                .orderDetails(List.of(orderDetail1, orderDetail2))
                .tribute(tribute)
                .price(expectedPrice)
                .sponsor(sponsor)
                .build();
        Mockito.doReturn(savedExpectedOrder).when(resourceOrderRepository).save(expectedOrder);

        ResourceOrderResponse resourceOrderResponse = sponsorService.sendResourcesForApproval(resourceOrderRequest);

        assertEquals(expectedPrice, resourceOrderResponse.getPrice());
        assertEquals(expectedUUID, resourceOrderResponse.getOrderId());

        Mockito.verify(securityUtil, Mockito.times(1)).getAuthenticatedUserId();
        Mockito.verify(resourceOrderRepository, Mockito.times(1)).save(expectedOrder);
    }

    @Test
    void sendResourcesTributeNotExists() {
        UUID sponsorId = new UUID(42, 43);
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);

        Mockito.when(sponsorRepository.findById(sponsorId))
                .thenReturn(Optional.of(sponsor));
        Mockito.when(securityUtil.getAuthenticatedUserId())
                .thenReturn(sponsorId);

        List<OrderDetailRequest> orderDetailRequestList = new ArrayList<>();
        UUID tributeId = new UUID(1, 1);

        ResourceOrderRequest resourceOrderRequest = new ResourceOrderRequest(tributeId, orderDetailRequestList);
        Throwable thrown = catchThrowable(() -> sponsorService.sendResourcesForApproval(resourceOrderRequest));

        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).contains("There's no tribute with the ID");
    }

    @Test
    void getOrdersNotPaidAndApproved() {
        UUID tributeId = new UUID(42, 42);
        String tributeName = "tribute";
        Tribute tribute = Tribute.builder()
                .id(tributeId)
                .name(tributeName)
                .build();

        Resource resource1 = Resource.builder()
                .id(new UUID(42, 42))
                .name("1")
                .price(BigDecimal.valueOf(100))
                .build();
        Resource resource2 = Resource.builder()
                .id(new UUID(42, 43))
                .name("2")
                .price(BigDecimal.valueOf(200))
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .resource(resource1)
                .size(1)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .resource(resource2)
                .size(2)
                .build();

        ResourceOrder resourceOrder1 = ResourceOrder.builder()
                .id(new UUID(42, 42))
                .approved(true)
                .orderDetails(List.of(orderDetail1))
                .tribute(tribute)
                .build();

        ResourceOrder resourceOrder2 = ResourceOrder.builder()
                .id(new UUID(42, 44))
                .approved(true)
                .orderDetails(List.of(orderDetail2))
                .tribute(tribute)
                .build();

        Mockito.doReturn(List.of(resourceOrder1, resourceOrder2)).when(resourceOrderRepository).findAllByPaidAndApproved(false, true);

        assertEquals(List.of(new ResourceApprovedAndNotPaidResponse(resourceOrder1), new ResourceApprovedAndNotPaidResponse(resourceOrder2)), sponsorService.getOrdersNotPaidAndApproved());
    }

    @Test
    void getSponsorById() {
        UUID sponsorId = new UUID(42, 43);
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);

        Mockito.doReturn(Optional.of(sponsor)).when(sponsorRepository).findById(sponsorId);

        assertEquals(new SponsorResponse(sponsor), sponsorService.getSponsorById(sponsorId));
        Mockito.verify(sponsorRepository, Mockito.times(1)).findById(sponsorId);
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
    void subscribeToNews() {
        String email = "example@email.com";

        UUID sponsorId = new UUID(42, 43);
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);

        Mockito.when(sponsorRepository.findById(sponsorId))
                .thenReturn(Optional.of(sponsor));
        Mockito.when(securityUtil.getAuthenticatedUserId())
                .thenReturn(sponsorId);

        NewsSubscriptionOrder expectedOrder = NewsSubscriptionOrder.builder()
                .sponsor(sponsor)
                .email(email)
                .price(ApplicationParameters.newsSubscriptionPrice)
                .build();

        UUID exceptedOrderId = new UUID(42, 0);

        NewsSubscriptionOrder expectedOrderSaved = NewsSubscriptionOrder.builder()
                .id(exceptedOrderId)
                .sponsor(sponsor)
                .email(email)
                .price(ApplicationParameters.newsSubscriptionPrice)
                .build();

        Mockito.doReturn(expectedOrderSaved).when(newsSubscriptionOrderRepository).save(expectedOrder);

        assertEquals(exceptedOrderId, sponsorService.subscribeToNews(new NewsSubscriptionOrderRequest(email)).getOrderId());

        sponsor.setNewsSubscriptionOrder(expectedOrderSaved);
        Mockito.verify(sponsorRepository, Mockito.times(1)).save(sponsor);
        Mockito.verify(securityUtil, Mockito.times(1)).getAuthenticatedUserId();
    }

    @Test
    void getNewsWithoutSubscription() {
        UUID sponsorId = new UUID(42, 43);
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);

        Mockito.when(sponsorRepository.findById(sponsorId))
                .thenReturn(Optional.of(sponsor));
        Mockito.when(securityUtil.getAuthenticatedUserId())
                .thenReturn(sponsorId);

        Throwable thrown = catchThrowable(() -> sponsorService.getNews());
        assertThat(thrown).isInstanceOf(NotNewsSubscribedException.class);
        assertThat(thrown.getMessage()).contains("You have no access to news. Buy subscription");
    }

    @Test
    void getNews() {
        UUID sponsorId = new UUID(42, 43);
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);
        sponsor.setNewsSubscriptionOrder(
                NewsSubscriptionOrder.builder()
                        .paid(true)
                        .build()
        );

        Mockito.when(sponsorRepository.findById(sponsorId))
                .thenReturn(Optional.of(sponsor));
        Mockito.when(securityUtil.getAuthenticatedUserId())
                .thenReturn(sponsorId);

        News news1 = News.builder()
                .name("name1")
                .content("content1")
                .dateTime(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();
        News news2 = News.builder()
                .name("name2")
                .content("content2")
                .dateTime(Instant.now().plus(2, ChronoUnit.DAYS))
                .build();
        News news3 = News.builder()
                .name("name3")
                .content("content3")
                .dateTime(Instant.now().minus(3, ChronoUnit.DAYS))
                .build();

        List<News> expectedNews = List.of(news1, news2, news3);
        List<NewsResponse> expectedNewsResponses = expectedNews.stream().map(NewsResponse::new).toList();

        Mockito.doReturn(expectedNews).when(newsRepository).findAll();

        assertEquals(expectedNewsResponses, sponsorService.getNews());
        Mockito.verify(securityUtil, Mockito.times(1)).getAuthenticatedUserId();
        Mockito.verify(newsRepository, Mockito.times(1)).findAll();
    }
}