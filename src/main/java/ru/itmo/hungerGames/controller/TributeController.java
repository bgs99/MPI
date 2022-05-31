package ru.itmo.hungerGames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungerGames.model.entity.Tribute;
import ru.itmo.hungerGames.model.request.AdvertisingTextRequest;
import ru.itmo.hungerGames.model.response.AdvertisingTextResponse;
import ru.itmo.hungerGames.service.ResourceSendingService;

import java.util.List;

@RestController
@RequestMapping("/api/tribute")
public class TributeController {
    private final ResourceSendingService resourceSendingService;

    @Autowired
    public TributeController(ResourceSendingService resourceSendingService) {
        this.resourceSendingService = resourceSendingService;
    }

    @GetMapping("/all")
    public List<Tribute> getAllTributes() {
        return resourceSendingService.getAllTributes();
    }

    @PostMapping("/advertisement")
    public AdvertisingTextResponse sendAdvertisingText(@RequestBody AdvertisingTextRequest advertisingTextRequest) {
        return resourceSendingService.sendAdvertisingText(advertisingTextRequest);
    }
}
