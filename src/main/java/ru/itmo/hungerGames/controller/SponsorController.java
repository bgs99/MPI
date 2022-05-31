package ru.itmo.hungerGames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.hungerGames.model.entity.Sponsor;
import ru.itmo.hungerGames.service.ResourceSendingService;

import java.util.List;

@RestController
@RequestMapping("/api/sponsor")
public class SponsorController {
    private final ResourceSendingService resourceSendingService;

    @Autowired
    public SponsorController(ResourceSendingService resourceSendingService) {
        this.resourceSendingService = resourceSendingService;
    }

    @GetMapping("/all")
    public List<Sponsor> getAllSponsors() {
        return resourceSendingService.getAllSponsors();
    }
}
