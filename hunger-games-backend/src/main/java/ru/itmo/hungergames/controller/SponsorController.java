package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.request.NewsSubscriptionOrderRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.*;
import ru.itmo.hungergames.service.SponsorService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public List<SponsorResponse> getAllSponsors() {
        return sponsorService.getAllSponsors();
    }

    @GetMapping("/{sponsorId}")
    public SponsorResponse getSponsorById(@PathVariable UUID sponsorId) {
        return sponsorService.getSponsorById(sponsorId);
    }

    @PostMapping("/send")
    public ResourceOrderResponse sendResourcesForApproval(@RequestBody ResourceOrderRequest resourceOrderRequest) {
        return sponsorService.sendResourcesForApproval(resourceOrderRequest);
    }

    @GetMapping("/orders/approved")
    public List<ResourceApprovedAndNotPaidResponse> getAllOrdersApprovedAndNotPaid() {
        return sponsorService.getOrdersNotPaidAndApproved();
    }

    @PostMapping("/subscription/news")
    public NewsSubscriptionOrderResponse subscribeToNews(@RequestBody NewsSubscriptionOrderRequest newsSubscriptionOrderRequest) {
        return sponsorService.subscribeToNews(newsSubscriptionOrderRequest);
    }

    @GetMapping("/subscription/price")
    public BigDecimal getNewsSubscriptionPrice() {
        return sponsorService.getPriceOfNewsSubscription();
    }

    @GetMapping("/news")
    public List<NewsResponse> getNews() {
        return sponsorService.getNews();
    }
}
