package ru.itmo.hungerGames.service;

import ru.itmo.hungerGames.model.entity.Mentor;
import ru.itmo.hungerGames.model.entity.Resource;
import ru.itmo.hungerGames.model.entity.Sponsor;
import ru.itmo.hungerGames.model.entity.Tribute;
import ru.itmo.hungerGames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungerGames.model.response.ResourceApprovalResponse;
import ru.itmo.hungerGames.model.response.SponsorResourceOrderResponse;

import java.util.List;

public interface ResourceSendingService {
    List<Tribute> getAllTributes();
    List<Sponsor> getAllSponsors();
    List<Mentor> getAllMentors();
    List<Resource> getAllResources();
    SponsorResourceOrderResponse sendResourcesForApproval(SponsorResourceOrderRequest sponsorResourceOrderRequest);
    List<ResourceApprovalResponse> getResourcesForApproval(Long mentorId);
}
