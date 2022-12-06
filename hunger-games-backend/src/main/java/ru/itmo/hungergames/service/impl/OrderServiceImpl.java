package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.hungergames.model.response.OrderResponse;
import ru.itmo.hungergames.repository.ResourceOrderRepository;
import ru.itmo.hungergames.service.OrderService;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final ResourceOrderRepository resourceOrderRepository;

    @Autowired
    public OrderServiceImpl(ResourceOrderRepository resourceOrderRepository) {
        this.resourceOrderRepository = resourceOrderRepository;
    }

    @Override
    public OrderResponse getOrderById(UUID orderId) {
        return new OrderResponse(resourceOrderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                        String.format("Order with id=%s doesn't exist", orderId))));
    }
}
