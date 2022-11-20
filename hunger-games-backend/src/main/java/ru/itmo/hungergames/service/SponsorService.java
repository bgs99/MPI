package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.entity.Sponsor;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.model.response.ResourceApprovedAndNotPaidResponse;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;

import java.util.List;

public interface SponsorService {
    List<Sponsor> getAllSponsors();
    ResourceOrderResponse sendResourcesForApproval(ResourceOrderRequest resourceOrderRequest);
    List<ResourceApprovedAndNotPaidResponse> getOrdersNotPaidAndApproved();
}
