package ru.itmo.hungerGames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.hungerGames.model.entity.*;
import ru.itmo.hungerGames.model.request.OrderDetailRequest;
import ru.itmo.hungerGames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungerGames.model.response.ResourceApprovalRequest;
import ru.itmo.hungerGames.model.response.SponsorResourceOrderResponse;
import ru.itmo.hungerGames.repository.*;
import ru.itmo.hungerGames.service.ResourceSendingService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceSendingServiceImpl implements ResourceSendingService {
    private final TributeRepository tributeRepository;
    private final SponsorRepository sponsorRepository;
    private final ResourceRepository resourceRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public ResourceSendingServiceImpl(TributeRepository tributeRepository, SponsorRepository sponsorRepository, ResourceRepository resourceRepository, OrdersRepository ordersRepository, OrderDetailRepository orderDetailRepository) {
        this.tributeRepository = tributeRepository;
        this.sponsorRepository = sponsorRepository;
        this.resourceRepository = resourceRepository;
        this.ordersRepository = ordersRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<Tribute> getAllTributes() {
        return tributeRepository.findAll();
    }

    @Override
    public List<Sponsor> getAllSponsors() {
        return sponsorRepository.findAll();
    }

    @Override
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    @Override
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
                .build();

        return SponsorResourceOrderResponse.builder()
                .id(ordersRepository.save(orders).getId())
                .price(price)
                .build();
    }

    @Override
    public List<OrderDetail> getResourcesForApproval(ResourceApprovalRequest resourceApprovalRequest) {
        return null;
    }
}
