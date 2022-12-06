package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.response.OrderResponse;

import java.util.UUID;

public interface OrderService {
    OrderResponse getOrderById(UUID orderId);
}
