package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.request.PaymentRequest;

public interface PaymentManagerService {
    void approvePayment(PaymentRequest paymentRequest);
}
