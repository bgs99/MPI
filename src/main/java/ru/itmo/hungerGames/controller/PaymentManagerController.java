package ru.itmo.hungerGames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.hungerGames.model.request.PaymentRequest;
import ru.itmo.hungerGames.service.PaymentManagerService;

@RestController
@RequestMapping("/api/payment")
public class PaymentManagerController {
    private final PaymentManagerService paymentManagerService;

    @Autowired
    public PaymentManagerController(PaymentManagerService paymentManagerService) {
        this.paymentManagerService = paymentManagerService;
    }

    @PostMapping
    public void setPaymentStatus(@RequestBody PaymentRequest paymentRequest) {
        paymentManagerService.setPaymentStatus(paymentRequest);
    }
}
