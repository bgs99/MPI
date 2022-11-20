package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.entity.Mentor;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;

import java.util.List;

public interface MentorService {
    List<Mentor> getAllMentors();
    List<ResourceApprovalResponse> getOrdersForApproval();
    void approveResourcesToSend(ApproveResourcesRequest approveResourcesRequest);
    ResourceOrderResponse sendResourcesToSponsor(ResourceOrderRequest resourceOrderRequest);
}
