package ru.itmo.hungerGames.service;

import ru.itmo.hungerGames.model.entity.OrderDetail;
import ru.itmo.hungerGames.model.entity.Resource;
import ru.itmo.hungerGames.model.entity.Sponsor;
import ru.itmo.hungerGames.model.entity.Tribute;
import ru.itmo.hungerGames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungerGames.model.response.ResourceApprovalRequest;
import ru.itmo.hungerGames.model.response.SponsorResourceOrderResponse;

import java.util.List;

public interface ResourceSendingService {
    List<Tribute> getAllTributes();
    List<Sponsor> getAllSponsors();
    List<Resource> getAllResources();
    SponsorResourceOrderResponse sendResourcesForApproval(SponsorResourceOrderRequest sponsorResourceOrderRequest);
    List<OrderDetail> getResourcesForApproval(ResourceApprovalRequest resourceApprovalRequest);
}
