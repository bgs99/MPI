package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.request.ApproveAdvertisingTextRequest;
import ru.itmo.hungergames.model.request.NewsRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.model.response.UserResponse;
import ru.itmo.hungergames.service.ModeratorService;

import java.util.List;

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

    @GetMapping
    public List<UserResponse> getAllModerators() {
        return moderatorService.getAllModerators();
    }

    @GetMapping("/advertisingText")
    public AdvertisingTextResponse getAnotherAdvertisingText() {
        return moderatorService.getAnotherAdvertisingText();
    }

    @PostMapping("/advertisingText/approve")
    public void approveAdvertisingText(
            @RequestBody ApproveAdvertisingTextRequest approveAdvertisingTextRequest) {
        moderatorService.approveAdvertisingText(approveAdvertisingTextRequest);
    }
}
