package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.entity.Resource;
import ru.itmo.hungergames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungergames.model.response.SponsorResourceOrderResponse;
import ru.itmo.hungergames.service.ResourceSendingService;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    private final ResourceSendingService resourceService;

    @Autowired
    public ResourceController(ResourceSendingService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/all")
    public List<Resource> getAllTributes() {
        return resourceService.getAllResources();
    }

    @PostMapping("/send")
    public SponsorResourceOrderResponse sendResourcesForApproval(@RequestBody SponsorResourceOrderRequest sponsorResourceOrderRequest) {
        return resourceService.sendResourcesForApproval(sponsorResourceOrderRequest);
    }
}
