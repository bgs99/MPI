package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.*;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.service.MentorService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MentorServiceImpl implements MentorService {
    private final MentorRepository mentorRepository;
    private final ResourceOrderRepository resourceOrderRepository;
    private final TributeRepository tributeRepository;
    private final ResourceRepository resourceRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final SecurityUtil securityUtil;

    @Autowired
    public MentorServiceImpl(MentorRepository mentorRepository,
                             ResourceOrderRepository resourceOrderRepository,
                             TributeRepository tributeRepository,
                             ResourceRepository resourceRepository,
                             OrderDetailRepository orderDetailRepository,
                             SecurityUtil securityUtil) {
        this.mentorRepository = mentorRepository;
        this.resourceOrderRepository = resourceOrderRepository;
        this.tributeRepository = tributeRepository;
        this.resourceRepository = resourceRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    @Override
    public List<ResourceApprovalResponse> getOrdersForApproval() {
        List<ResourceOrder> orders = resourceOrderRepository
                .findAllByTribute_MentorIdAndPaidAndApproved(
                        securityUtil.getAuthenticatedUser().getId(), true, null);
        return orders.stream().map(ResourceApprovalResponse::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void approveResourcesToSend(ApproveResourcesRequest approveResourcesRequest) {
        Mentor mentor = mentorRepository
                .findById(securityUtil.getAuthenticatedUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no mentor with such ID"));
        Optional<ResourceOrder> orderOptional = resourceOrderRepository
                .findByIdAndTribute_MentorIdAndPaid(
                        approveResourcesRequest.getOrderId(), mentor.getId(), true);

        if (!orderOptional.isPresent()) {
            return;
        }
        ResourceOrder order = orderOptional.get();
        order.setApproved(approveResourcesRequest.getApproved());
        resourceOrderRepository.save(order);
    }

    @Override
    @Transactional
    public ResourceOrderResponse sendResourcesToSponsor(ResourceOrderRequest resourceOrderRequest) {
        Tribute tribute = tributeRepository
                .findById(resourceOrderRequest.getTributeId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with the ID"));

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
                .price(price)
                .approved(true)
                .build();

        return ResourceOrderResponse.builder()
                .orderId(resourceOrderRepository.save(order).getId())
                .price(price)
                .build();
    }

    @Override
    public List<TributeResponse> getAllTributes() {
        return tributeRepository.findAllByMentor_Id(securityUtil.getAuthenticatedUserId())
                .stream().map(TributeResponse::new).collect(Collectors.toList());
    }
}
