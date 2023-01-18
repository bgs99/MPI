package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.request.ApproveAdvertisingTextRequest;
import ru.itmo.hungergames.model.request.NewsRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.model.response.JwtResponse;
import ru.itmo.hungergames.model.response.UserResponse;

import java.util.List;

public interface ModeratorService {
    void publishNews(NewsRequest newsRequest);

    List<UserResponse> getAllModerators();

    AdvertisingTextResponse getAnotherAdvertisingText();

    void approveAdvertisingText(ApproveAdvertisingTextRequest approveAdvertisingTextRequest);
}
