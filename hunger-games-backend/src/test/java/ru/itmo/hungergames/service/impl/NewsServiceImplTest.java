package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.News;
import ru.itmo.hungergames.model.entity.user.Moderator;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.response.NewsResponse;
import ru.itmo.hungergames.repository.NewsRepository;
import ru.itmo.hungergames.repository.SponsorRepository;
import ru.itmo.hungergames.util.EmailSender;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
class NewsServiceImplTest {

    @Autowired
    NewsServiceImpl newsService;
    @MockBean
    EmailSender emailSender;
    @MockBean
    NewsRepository newsRepository;
    @MockBean
    SponsorRepository sponsorRepository;

    @Test
    void sendNewsToAllSubscribers() {
        Sponsor sponsor = Sponsor.builder().build();

        News news1 = News.builder()
                .id(new UUID(42, 43))
                .content("news1")
                .moderator(Moderator.builder().build())
                .dateTime(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();
        News news2 = News.builder()
                .id(new UUID(42, 43))
                .content("news2")
                .moderator(Moderator.builder().build())
                .dateTime(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();
        News oldNews = News.builder()
                .id(new UUID(42, 43))
                .content("news3")
                .moderator(Moderator.builder().build())
                .dateTime(Instant.now().minus(1, ChronoUnit.DAYS))
                .build();

        var expectedNews = List.of(new NewsResponse(news1), new NewsResponse(news2));

        Mockito.doReturn(List.of(news1, news2, oldNews)).when(newsRepository).findAll();
        Mockito.doReturn(List.of(sponsor)).when(sponsorRepository).findAllByNewsSubscriptionOrder_paid(true);

        newsService.sendNewsToAllSubscribers();

        Mockito.verify(emailSender, Mockito.times(1)).sendNews(sponsor, expectedNews);
    }
}