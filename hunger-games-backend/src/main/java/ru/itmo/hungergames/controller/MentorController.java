package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.MentorResponse;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.service.MentorService;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@PreAuthorize("hasRole('MENTOR')")
@RequestMapping("/api/mentor")
public class MentorController {
    private final MentorService mentorService;

    @Autowired
    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public List<MentorResponse> getAllMentors() {
        return mentorService.getAllMentors();
    }

    @GetMapping("/{id}")
    public MentorResponse getMentorById(@PathVariable UUID id) {
        return mentorService.getMentorById(id);
    }

    @GetMapping("/orders")
    public List<ResourceApprovalResponse> getOrdersForApproval() {
        return mentorService.getOrdersForApproval();
    }

    @PostMapping("/order/approve")
    public void approveResourcesToSend(@RequestBody ApproveResourcesRequest approveResourcesRequest) {
        mentorService.approveResourcesToSend(approveResourcesRequest);
    }

    @PostMapping("/order/create")
    public void createOrder(@RequestBody ResourceOrderRequest resourceOrderRequest) {
        mentorService.sendResourcesToSponsor(resourceOrderRequest);
    }

    @GetMapping("/tribute/all")
    public List<TributeResponse> getMentorTributes() {
        return mentorService.getAllTributes();
    }
}
