package ru.itmo.hungerGames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.hungerGames.model.entity.Resource;
import ru.itmo.hungerGames.service.ResourceSendingService;

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
}
