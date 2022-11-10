package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.entity.Mentor;
import ru.itmo.hungergames.model.entity.Resource;
import ru.itmo.hungergames.model.entity.Sponsor;
import ru.itmo.hungergames.model.entity.Tribute;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.model.response.SponsorResourceOrderResponse;

import java.util.List;

public interface ResourceSendingService {
    List<Tribute> getAllTributes();
    List<Sponsor> getAllSponsors();
    List<Mentor> getAllMentors();
    List<Resource> getAllResources();
    SponsorResourceOrderResponse sendResourcesForApproval(SponsorResourceOrderRequest sponsorResourceOrderRequest);
    List<ResourceApprovalResponse> getOrdersForApproval(Long mentorId);
    void approveResourcesToSend(ApproveResourcesRequest approveResourcesRequest);
    AdvertisingTextResponse sendAdvertisingText(AdvertisingTextRequest advertisingTextRequest);
}
