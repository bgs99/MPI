package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.News;
import ru.itmo.hungergames.model.request.NewsRequest;
import ru.itmo.hungergames.model.response.UserResponse;
import ru.itmo.hungergames.repository.MessageRepository;
import ru.itmo.hungergames.repository.NewsRepository;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(value = {"/initScripts/create-moderators-and-news.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/initScripts/drop-moderators-and-news.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ModeratorServiceImplTest {
    @Autowired
    ModeratorServiceImpl moderatorService;
    @MockBean
    SecurityUtil securityUtil;
    @SpyBean
    NewsRepository newsRepository;
    @SpyBean
    MessageRepository moderatorRepository;
    @Test
    void publishNews() {
        UUID moderatorId = UUID.fromString("76f1316a-3eec-46c6-a323-bf3060a3c72a");
        String initialName = "name-test";
        String initialContent = "content-test";

        Mockito.doReturn(moderatorId)
                .when(securityUtil).getAuthenticatedUserId();

        moderatorService.publishNews(new NewsRequest(initialName, initialContent));
        List<News> newsFromDb = newsRepository.findAll();

        assertTrue(newsFromDb.stream().map(News::getContent).toList().contains(initialContent));
        assertTrue(newsFromDb.stream().map(News::getName).toList().contains(initialName));
    }

    @Test
    void getAllModerators() {
        List<UUID> initialUUIDs = List.of(
                UUID.fromString("76f1316a-3eec-46c6-a323-bf3060a3c72a"),
                UUID.fromString("55d084b4-86f0-4c37-92b1-eba177d4cb44")
        );

        List<UserResponse> allModerators = moderatorService.getAllModerators();
        List<UUID> uuids = allModerators.stream().map(UserResponse::getId).toList();

        assertTrue(uuids.containsAll(initialUUIDs));
    }
}