package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.entity.Sponsor;
import ru.itmo.hungergames.model.request.SponsorResourceOrderRequest;
import ru.itmo.hungergames.model.response.SponsorResourceOrderResponse;

import java.util.List;

public interface SponsorService {
    List<Sponsor> getAllSponsors();
    SponsorResourceOrderResponse sendResourcesForApproval(SponsorResourceOrderRequest sponsorResourceOrderRequest); // TO sp
}
