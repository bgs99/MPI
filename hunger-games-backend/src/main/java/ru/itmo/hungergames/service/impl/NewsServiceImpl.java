package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.response.NewsResponse;
import ru.itmo.hungergames.repository.NewsRepository;
import ru.itmo.hungergames.repository.SponsorRepository;
import ru.itmo.hungergames.service.NewsService;
import ru.itmo.hungergames.util.EmailSender;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final SponsorRepository sponsorRepository;
    private final EmailSender emailSender;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository,
                           SponsorRepository sponsorRepository,
                           EmailSender emailSender) {
        this.newsRepository = newsRepository;
        this.sponsorRepository = sponsorRepository;
        this.emailSender = emailSender;
    }

    @Override
    @Transactional
    public void sendNewsToAllSubscribers() {
        List<NewsResponse> newsResponses = newsRepository
                .findAll().stream()
                .map(NewsResponse::new)
                .toList();
        List<Sponsor> sponsors = sponsorRepository.findAllByNewsSubscriptionOrder_paid(true);
        for (Sponsor sponsor : sponsors) {
            emailSender.sendNews(sponsor, newsResponses);
        }
    }
}
