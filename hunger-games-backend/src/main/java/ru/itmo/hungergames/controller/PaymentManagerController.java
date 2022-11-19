package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.request.PaymentRequest;
import ru.itmo.hungergames.service.PaymentManagerService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/payment") // TODO: certs identification with pub keys decrypt
public class PaymentManagerController {
    private final PaymentManagerService paymentManagerService;

    @Autowired
    public PaymentManagerController(PaymentManagerService paymentManagerService) {
        this.paymentManagerService = paymentManagerService;
    }

    @PostMapping("/approve")
    public void approvePayment(@RequestBody PaymentRequest paymentRequest) {
        paymentManagerService.approvePayment(paymentRequest);
    }
}
