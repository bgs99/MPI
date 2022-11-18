package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.*;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungergames.model.response.SponsorResourceOrderResponse;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.service.SponsorService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SponsorServiceImpl implements SponsorService {
    private final SponsorRepository sponsorRepository;
    private final TributeRepository tributeRepository;
    private final ResourceRepository resourceRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrdersRepository ordersRepository;

    @Autowired
    public SponsorServiceImpl(SponsorRepository sponsorRepository,
                              TributeRepository tributeRepository,
                              ResourceRepository resourceRepository,
                              OrderDetailRepository orderDetailRepository,
                              OrdersRepository ordersRepository) {
        this.sponsorRepository = sponsorRepository;
        this.tributeRepository = tributeRepository;
        this.resourceRepository = resourceRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.ordersRepository = ordersRepository;
    }

    @Override
    public List<Sponsor> getAllSponsors() {
        return sponsorRepository.findAll();
    }

    @Override
    @Transactional
    public SponsorResourceOrderResponse sendResourcesForApproval(SponsorResourceOrderRequest sponsorResourceOrderRequest) {
        Tribute tribute = tributeRepository
                .findById(sponsorResourceOrderRequest.getTributeId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with the ID"));
        Sponsor sponsor = sponsorRepository
                .findById(sponsorResourceOrderRequest.getSponsorId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no sponsor with the ID"));

        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal price = new BigDecimal(0);
        for (OrderDetailRequest orderDetailRequest : sponsorResourceOrderRequest.getOrderDetails()) {
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
        Orders orders = Orders.builder()
                .orderDetails(orderDetails)
                .tribute(tribute)
                .sponsor(sponsor)
                .price(price)
                .ordersType(OrdersType.RESOURCES)
                .build();

        return SponsorResourceOrderResponse.builder()
                .orderId(ordersRepository.save(orders).getId())
                .price(price)
                .build();
    }
}
