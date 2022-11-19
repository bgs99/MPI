package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.entity.Mentor;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.service.MentorService;

import java.util.List;

@RestController
@PreAuthorize("hasRole('MENTOR')")
@RequestMapping("/api/mentor")
public class MentorController {
    private final MentorService mentorService;

    @Autowired
    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @GetMapping("/all")
    public List<Mentor> getAllMentors() {
        return mentorService.getAllMentors();
    }

    @GetMapping("/orders")
    public List<ResourceApprovalResponse> getOrdersForApproval(@RequestParam Long mentorId) {
        return mentorService.getOrdersForApproval(mentorId);
    }

    @PostMapping("/order/approve")
    public void approveResourcesToSend(@RequestBody ApproveResourcesRequest approveResourcesRequest) {
        mentorService.approveResourcesToSend(approveResourcesRequest);
    }

    // TODO: создание ордера, который апрувнут, но не оплачен (на основе трибута)
}
