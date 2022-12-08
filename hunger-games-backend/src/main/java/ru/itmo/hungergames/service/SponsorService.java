package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.ResourceApprovedAndNotPaidResponse;
import ru.itmo.hungergames.model.response.ResourceOrderResponse;
import ru.itmo.hungergames.model.response.SponsorResponse;

import java.util.List;
import java.util.UUID;

public interface SponsorService {
    List<SponsorResponse> getAllSponsors();
    ResourceOrderResponse sendResourcesForApproval(ResourceOrderRequest resourceOrderRequest);
    List<ResourceApprovedAndNotPaidResponse> getOrdersNotPaidAndApproved();
    SponsorResponse getSponsorById(UUID sponsorId);
}
