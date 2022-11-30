package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.MentorResponse;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;
import ru.itmo.hungergames.model.response.TributeResponse;

import java.util.List;
import java.util.UUID;

public interface MentorService {
    List<MentorResponse> getAllMentors();
    List<ResourceApprovalResponse> getOrdersForApproval();
    void approveResourcesToSend(ApproveResourcesRequest approveResourcesRequest);
    ResourceOrderResponse sendResourcesToSponsor(ResourceOrderRequest resourceOrderRequest);
    List<TributeResponse> getAllTributes();
    MentorResponse getMentorById(UUID mentorId);
}
