package ru.itmo.hungerGames.service;

import ru.itmo.hungerGames.model.entity.*;
import ru.itmo.hungerGames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungerGames.model.response.ResourceApprovalRequest;
import ru.itmo.hungerGames.model.response.SponsorResourceOrderResponse;

import java.util.List;

public interface ResourceSendingService {
    List<Tribute> getAllTributes();
    List<Sponsor> getAllSponsors();
    List<Mentor> getAllMentors();
    List<Resource> getAllResources();
    SponsorResourceOrderResponse sendResourcesForApproval(SponsorResourceOrderRequest sponsorResourceOrderRequest);
    List<OrderDetail> getResourcesForApproval(ResourceApprovalRequest resourceApprovalRequest);
}
