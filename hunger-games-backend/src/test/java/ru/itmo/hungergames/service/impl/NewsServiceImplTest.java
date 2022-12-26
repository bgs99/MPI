package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.repository.NewsRepository;
import ru.itmo.hungergames.repository.SponsorRepository;
import ru.itmo.hungergames.util.EmailSender;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
class NewsServiceImplTest {

    @Autowired
    NewsServiceImpl newsService;
    @MockBean
    EmailSender emailSender;
    @SpyBean
    NewsRepository newsRepository;
    @SpyBean
    SponsorRepository sponsorRepository;

    @Test
    @Sql(value = {"/initScripts/create-multiple-sponsors.sql", "/initScripts/create-moderators-and-news.sql", "/initScripts/create-sponsor-with-subscription.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-multiple-sponsors.sql", "/initScripts/drop-moderators-and-news.sql", "/initScripts/drop-sponsor-with-subscription.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendNewsToAllSubscribers() {
        UUID subscribedSponsorUUID = UUID.fromString("523adea8-9f98-41a3-bae0-1e4875aceaae");

        newsService.sendNewsToAllSubscribers();

        Mockito.verify(newsRepository, Mockito.times(1)).findAll();
        Mockito.verify(sponsorRepository, Mockito.times(1)).findAllByNewsSubscriptionOrder_paid(true);
        Mockito.verify(emailSender, Mockito.times(1)).sendNews(Mockito.argThat((Sponsor sponsor) -> (sponsor.getId().equals(subscribedSponsorUUID))), ArgumentMatchers.anyList());
    }
}