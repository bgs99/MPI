package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.request.NewsRequest;
import ru.itmo.hungergames.service.ModeratorService;

@CrossOrigin("*")
@RestController
@PreAuthorize("hasRole('MODERATOR')")
@RequestMapping("/api/moderator")
public class ModeratorController {
    private final ModeratorService moderatorService;

    @Autowired
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @PostMapping("/publish/news")
    public void publishNews(@RequestBody NewsRequest newsRequest) {
        moderatorService.publishNews(newsRequest);
    }
}
