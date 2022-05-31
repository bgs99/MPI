package ru.itmo.hungerGames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.hungerGames.model.entity.Orders;
import ru.itmo.hungerGames.model.request.PaymentRequest;
import ru.itmo.hungerGames.repository.OrdersRepository;
import ru.itmo.hungerGames.service.PaymentManagerService;

@Service
public class PaymentManagerServiceImpl implements PaymentManagerService {
    private final OrdersRepository ordersRepository;

    @Autowired
    public PaymentManagerServiceImpl(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    public void approvePayment(PaymentRequest paymentRequest) {
        Orders orders = ordersRepository
                .findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order is not found."));
        orders.setPaid(true);
        ordersRepository.save(orders);
    }
}
