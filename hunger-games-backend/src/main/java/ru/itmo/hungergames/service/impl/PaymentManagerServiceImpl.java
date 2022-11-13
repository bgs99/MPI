package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.Orders;
import ru.itmo.hungergames.model.request.PaymentRequest;
import ru.itmo.hungergames.repository.OrdersRepository;
import ru.itmo.hungergames.service.PaymentManagerService;

@Service
@Transactional(readOnly = true)
public class PaymentManagerServiceImpl implements PaymentManagerService {
    private final OrdersRepository ordersRepository;

    @Autowired
    public PaymentManagerServiceImpl(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    @Transactional
    public void approvePayment(PaymentRequest paymentRequest) {
        Orders orders = ordersRepository
                .findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order is not found."));
        orders.setPaid(true);
        ordersRepository.save(orders);
    }
}
