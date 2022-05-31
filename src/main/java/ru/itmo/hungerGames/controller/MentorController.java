package ru.itmo.hungerGames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.hungerGames.model.entity.Mentor;
import ru.itmo.hungerGames.service.ResourceSendingService;

import java.util.List;

@RestController("/api/mentor")
public class MentorController {
    private final ResourceSendingService resourceSendingService;

    @Autowired
    public MentorController(ResourceSendingService resourceSendingService) {
        this.resourceSendingService = resourceSendingService;
    }

    @GetMapping("/all")
    public List<Mentor> getAllMentors() {
        return resourceSendingService.getAllMentors();
    }
}
