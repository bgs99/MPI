package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.News;
import ru.itmo.hungergames.model.entity.user.Moderator;
import ru.itmo.hungergames.model.request.NewsRequest;
import ru.itmo.hungergames.repository.ModeratorRepository;
import ru.itmo.hungergames.repository.NewsRepository;
import ru.itmo.hungergames.service.ModeratorService;
import ru.itmo.hungergames.util.SecurityUtil;

@Service
@Transactional(readOnly = true)
public class ModeratorServiceImpl implements ModeratorService {
    private final ModeratorRepository moderatorRepository;
    private final NewsRepository newsRepository;
    private final SecurityUtil securityUtil;

    @Autowired
    public ModeratorServiceImpl(ModeratorRepository moderatorRepository, NewsRepository newsRepository, SecurityUtil securityUtil) {
        this.moderatorRepository = moderatorRepository;
        this.newsRepository = newsRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional
    public void publishNews(NewsRequest newsRequest) {
        Moderator moderator = moderatorRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no moderator with such ID"));
        newsRepository.save(News.builder()
                .name(newsRequest.getName())
                .content(newsRequest.getContent())
                .moderator(moderator)
                .build());
    }
}
