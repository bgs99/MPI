package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.request.NewsSubscriptionOrderRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface SponsorService {
    List<SponsorResponse> getAllSponsors();
    ResourceOrderResponse sendResourcesForApproval(ResourceOrderRequest resourceOrderRequest);
    List<ResourceApprovedAndNotPaidResponse> getOrdersNotPaidAndApproved();
    SponsorResponse getSponsorById(UUID sponsorId);
    NewsSubscriptionOrderResponse subscribeToNews(NewsSubscriptionOrderRequest newsSubscriptionOrderRequest);
    BigDecimal getPriceOfNewsSubscription();
    List<NewsResponse> getNews();

    boolean isSubscribed();
}
