package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.entity.Sponsor;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.ResourceApprovedAndNotPaidResponse;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;
import ru.itmo.hungergames.service.SponsorService;

import java.util.List;

@CrossOrigin("*")
@RestController
@PreAuthorize("hasRole('SPONSOR')")
@RequestMapping("/api/sponsor")
public class SponsorController {
    private final SponsorService sponsorService;

    @Autowired
    public SponsorController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @PreAuthorize("hasAnyRole('TRIBUTE', 'MENTOR', 'SPONSOR')")
    @GetMapping("/all")
    public List<Sponsor> getAllSponsors() {
        return sponsorService.getAllSponsors();
    }

    @PostMapping("/send")
    public ResourceOrderResponse sendResourcesForApproval(@RequestBody ResourceOrderRequest resourceOrderRequest) {
        return sponsorService.sendResourcesForApproval(resourceOrderRequest);
    }

    @GetMapping("/orders/approved")
    public List<ResourceApprovedAndNotPaidResponse> getAllOrdersApprovedAndNotPaid() {
        return sponsorService.getOrdersNotPaidAndApproved();
    }
}
