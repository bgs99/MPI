package ru.itmo.hungergames.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.itmo.hungergames.service.NewsService;

@Configuration
@EnableScheduling
@Slf4j
public class Schedule {
    private final NewsService newsService;

    @Autowired
    public Schedule(NewsService newsService) {
        this.newsService = newsService;
    }

    @Scheduled(cron = "${hunger-games.schedule.sendNews.cron}")
    public void sendNews() {
        newsService.sendNewsToAllSubscribers();
        log.info("News are send to subscribers");
    }
}
