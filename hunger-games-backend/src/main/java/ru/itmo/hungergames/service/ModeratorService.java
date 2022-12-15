package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.request.NewsRequest;

public interface ModeratorService {
    void publishNews(NewsRequest newsRequest);
}
