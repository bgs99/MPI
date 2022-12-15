package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.order.NewsSubscriptionOrder;
import ru.itmo.hungergames.model.entity.order.OrderDetail;
import ru.itmo.hungergames.model.entity.order.Resource;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.request.NewsSubscriptionOrderRequest;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.*;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.service.SponsorService;
import ru.itmo.hungergames.util.ApplicationParameters;
import ru.itmo.hungergames.util.SecurityUtil;
import ru.itmo.hungergames.util.SponsorUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SponsorServiceImpl implements SponsorService {
    private final SponsorRepository sponsorRepository;
    private final TributeRepository tributeRepository;
    private final ResourceRepository resourceRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ResourceOrderRepository resourceOrderRepository;
    private final NewsSubscriptionOrderRepository newsSubscriptionOrderRepository;
    private final NewsRepository newsRepository;
    private final SecurityUtil securityUtil;
    private final SponsorUtil sponsorUtil;

    @Autowired
    public SponsorServiceImpl(SponsorRepository sponsorRepository,
                              TributeRepository tributeRepository,
                              ResourceRepository resourceRepository,
                              OrderDetailRepository orderDetailRepository,
                              ResourceOrderRepository resourceOrderRepository,
                              NewsSubscriptionOrderRepository newsSubscriptionOrderRepository,
                              NewsRepository newsRepository,
                              SecurityUtil securityUtil,
                              SponsorUtil sponsorUtil) {
        this.sponsorRepository = sponsorRepository;
        this.tributeRepository = tributeRepository;
        this.resourceRepository = resourceRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.resourceOrderRepository = resourceOrderRepository;
        this.newsSubscriptionOrderRepository = newsSubscriptionOrderRepository;
        this.newsRepository = newsRepository;
        this.securityUtil = securityUtil;
        this.sponsorUtil = sponsorUtil;
    }

    @Override
    public List<SponsorResponse> getAllSponsors() {
        return sponsorRepository
                .findAll().stream()
                .map(SponsorResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResourceOrderResponse sendResourcesForApproval(ResourceOrderRequest resourceOrderRequest) {
        Tribute tribute = tributeRepository
                .findById(resourceOrderRequest.getTributeId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with the ID"));
        Sponsor sponsor = sponsorRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no sponsor with the ID"));

        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal price = new BigDecimal(0);
        for (OrderDetailRequest orderDetailRequest : resourceOrderRequest.getOrderDetails()) {
            Optional<Resource> resourceOptional = resourceRepository.findById(orderDetailRequest.getResourceId());
            if (resourceOptional.isEmpty()) {
                continue;
            }
            Resource resource = resourceOptional.get();
            orderDetails.add(
                    orderDetailRepository.save(OrderDetail.builder()
                            .size(orderDetailRequest.getSize())
                            .resource(resource)
                            .build())
            );

            price = price.add(resource.getPrice().multiply(BigDecimal.valueOf(orderDetailRequest.getSize())));
        }
        ResourceOrder order = ResourceOrder.builder()
                .orderDetails(orderDetails)
                .tribute(tribute)
                .sponsor(sponsor)
                .price(price)
                .build();

        return ResourceOrderResponse.builder()
                .orderId(resourceOrderRepository.save(order).getId())
                .price(price)
                .build();
    }

    @Override
    public List<ResourceApprovedAndNotPaidResponse> getOrdersNotPaidAndApproved() {
        List<ResourceOrder> orders = resourceOrderRepository
                .findAllByPaidAndApproved(false, true);
        return orders.stream().map(ResourceApprovedAndNotPaidResponse::new).collect(Collectors.toList());
    }

    @Override
    public SponsorResponse getSponsorById(UUID sponsorId) {
        return new SponsorResponse(sponsorRepository.findById(sponsorId).orElseThrow(() -> new ResourceNotFoundException(String.format("Sponsor with id=%s doesn't exist", sponsorId))));
    }

    @Override
    @Transactional
    public NewsSubscriptionOrderResponse subscribeToNews(NewsSubscriptionOrderRequest newsSubscriptionOrderRequest) {
        Sponsor sponsor = sponsorRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("There's no sponsor with id=%s", securityUtil.getAuthenticatedUserId())));
        NewsSubscriptionOrder order = NewsSubscriptionOrder.builder()
                .sponsor(sponsor)
                .email(newsSubscriptionOrderRequest.getEmail())
                .build();

        return NewsSubscriptionOrderResponse.builder()
                .subscribeOrderId(newsSubscriptionOrderRepository.save(order).getId())
                .build();
    }

    @Override
    public BigDecimal getPriceOfNewsSubscription() {
        return ApplicationParameters.newsSubscriptionPrice;
    }

    @Override
    public List<NewsResponse> getNews() {
        sponsorUtil.checkIfSponsorCanSeeNews(sponsorRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format(
                                "There's no sponsor with id=%s", securityUtil.getAuthenticatedUserId()))));
        return newsRepository
                .findAll().stream()
                .map(NewsResponse::new)
                .toList();
    }
}
