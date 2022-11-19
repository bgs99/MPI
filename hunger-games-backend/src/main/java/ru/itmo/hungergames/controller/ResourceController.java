package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.hungergames.model.entity.Resource;
import ru.itmo.hungergames.service.ResourceService;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/all")
    public List<Resource> getAllResources() {
        return resourceService.getAllResources();
    }
}
