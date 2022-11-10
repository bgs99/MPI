package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.hungergames.model.entity.*;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.model.response.SponsorResourceOrderResponse;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.service.ResourceSendingService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResourceSendingServiceImpl implements ResourceSendingService {
    private final TributeRepository tributeRepository;
    private final SponsorRepository sponsorRepository;
    private final MentorRepository mentorRepository;
    private final ResourceRepository resourceRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public ResourceSendingServiceImpl(TributeRepository tributeRepository, SponsorRepository sponsorRepository, MentorRepository mentorRepository, ResourceRepository resourceRepository, OrdersRepository ordersRepository, OrderDetailRepository orderDetailRepository) {
        this.tributeRepository = tributeRepository;
        this.sponsorRepository = sponsorRepository;
        this.mentorRepository = mentorRepository;
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
    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
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
                .ordersType(OrdersType.RESOURCES)
                .build();

        return SponsorResourceOrderResponse.builder()
                .orderId(ordersRepository.save(orders).getId())
                .price(price)
                .build();
    }

    @Override
    public List<ResourceApprovalResponse> getOrdersForApproval(Long mentorId) {
        List<Orders> orders = ordersRepository
                .findAllByTribute_MentorIdAndPaidAndApprovedAndOrdersType(mentorId, true, null, OrdersType.RESOURCES);
        return orders.stream().map(ResourceApprovalResponse::new).collect(Collectors.toList());
    }

    @Override
    public void approveResourcesToSend(ApproveResourcesRequest approveResourcesRequest) {
        Mentor mentor = mentorRepository
                .findById(approveResourcesRequest.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no mentor with such ID"));
        Optional<Orders> ordersOptional = ordersRepository
                .findByIdAndTribute_MentorIdAndPaid(
                        approveResourcesRequest.getOrderId(), mentor.getId(), true);
        if (!ordersOptional.isPresent()) {
            return;
        }
        Orders orders = ordersOptional.get();
        orders.setApproved(approveResourcesRequest.getApproved());
        ordersRepository.save(orders);
    }

    @Override
    public AdvertisingTextResponse sendAdvertisingText(AdvertisingTextRequest advertisingTextRequest) {
        Tribute tribute = tributeRepository
                .findById(advertisingTextRequest.getTributeId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with such ID"));

        Orders order = ordersRepository.save(Orders.builder()
                .tribute(tribute)
                .advertisingText(advertisingTextRequest.getText())
                .ordersType(OrdersType.ADVERTISEMENT)
                .price(BigDecimal.valueOf(200))
                .build());

        return new AdvertisingTextResponse(
                order.getId(),
                order.getPrice()
        );
    }
}