package ru.itmo.hungerGames.service;

import ru.itmo.hungerGames.model.request.PaymentRequest;

public interface PaymentManagerService {
    void setPaymentStatus(PaymentRequest paymentRequest);
}
