package ru.itmo.hungerGames.service;

import ru.itmo.hungerGames.model.entity.Resource;
import ru.itmo.hungerGames.model.entity.Tribute;
import ru.itmo.hungerGames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungerGames.model.response.SponsorResourceOrderResponse;

import java.util.List;

public interface ResourceSendingService {
    List<Tribute> getAllTributes();
    List<Resource> getAllResources();
    SponsorResourceOrderResponse sendResourcesToMentorForApproval(SponsorResourceOrderRequest sponsorResourceOrderRequest);
}
