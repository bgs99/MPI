package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.entity.Sponsor;
import ru.itmo.hungergames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungergames.model.response.SponsorResourceOrderResponse;
import ru.itmo.hungergames.service.SponsorService;

import java.util.List;

@RestController
@RequestMapping("/api/sponsor")
public class SponsorController {
    private final SponsorService sponsorService;

    @Autowired
    public SponsorController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @GetMapping("/all")
    public List<Sponsor> getAllSponsors() {
        return sponsorService.getAllSponsors();
    }

    @PostMapping("/send")
    public SponsorResourceOrderResponse sendResourcesForApproval(@RequestBody SponsorResourceOrderRequest sponsorResourceOrderRequest) {
        return sponsorService.sendResourcesForApproval(sponsorResourceOrderRequest);
    }

    // TODO: получение ордера, который апрувнут, но не оплачен (инпут: order and tri
}
