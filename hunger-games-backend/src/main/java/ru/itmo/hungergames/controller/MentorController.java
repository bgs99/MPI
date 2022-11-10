package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.entity.Mentor;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.service.ResourceSendingService;

import java.util.List;

@RestController
@RequestMapping("/api/mentor")
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

    @GetMapping("/orders")
    public List<ResourceApprovalResponse> getOrdersForApproval(@RequestParam Long mentorId) {
        return resourceSendingService.getOrdersForApproval(mentorId);
    }

    @PostMapping("/order/approve")
    public void approveResourcesToSend(@RequestBody ApproveResourcesRequest approveResourcesRequest) {
        resourceSendingService.approveResourcesToSend(approveResourcesRequest);
    }
}
