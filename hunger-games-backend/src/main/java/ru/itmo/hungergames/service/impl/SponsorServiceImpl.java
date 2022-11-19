package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.*;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.model.response.ResourceApprovedAndNotPaidResponse;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.service.SponsorService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SponsorServiceImpl implements SponsorService {
    private final SponsorRepository sponsorRepository;
    private final TributeRepository tributeRepository;
    private final ResourceRepository resourceRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ResourceOrderRepository resourceOrderRepository;
    private final SecurityUtil securityUtil;

    @Autowired
    public SponsorServiceImpl(SponsorRepository sponsorRepository,
                              TributeRepository tributeRepository,
                              ResourceRepository resourceRepository,
                              OrderDetailRepository orderDetailRepository,
                              ResourceOrderRepository resourceOrderRepository,
                              SecurityUtil securityUtil) {
        this.sponsorRepository = sponsorRepository;
        this.tributeRepository = tributeRepository;
        this.resourceRepository = resourceRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.resourceOrderRepository = resourceOrderRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public List<Sponsor> getAllSponsors() {
        return sponsorRepository.findAll();
    }

    @Override
    @Transactional
    public ResourceOrderResponse sendResourcesForApproval(ResourceOrderRequest resourceOrderRequest) {
        Tribute tribute = tributeRepository
                .findById(resourceOrderRequest.getTributeId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with the ID"));
        Sponsor sponsor = sponsorRepository
                .findById(securityUtil.getAuthenticatedUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no sponsor with the ID"));

        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal price = new BigDecimal(0);
        for (OrderDetailRequest orderDetailRequest : resourceOrderRequest.getOrderDetails()) {
            Optional<Resource> resourceOptional = resourceRepository.findById(orderDetailRequest.getResourceId());
            if (!resourceOptional.isPresent()) {
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
}
