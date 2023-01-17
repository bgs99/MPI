package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.News;
import ru.itmo.hungergames.model.entity.user.Moderator;
import ru.itmo.hungergames.model.request.NewsRequest;
import ru.itmo.hungergames.model.response.UserResponse;
import ru.itmo.hungergames.repository.ModeratorRepository;
import ru.itmo.hungergames.repository.NewsRepository;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
class ModeratorServiceImplTest {
    @Autowired
    ModeratorServiceImpl moderatorService;
    @MockBean
    SecurityUtil securityUtil;
    @MockBean
    NewsRepository newsRepository;
    @MockBean
    ModeratorRepository moderatorRepository;

    @Test
    void publishNews() {
        UUID moderatorId = new UUID(42, 42);
        Moderator moderator = Moderator.builder()
                .id(moderatorId)
                .username("username")
                .build();
        String initialName = "name-test";
        String initialContent = "content-test";

        Mockito.doReturn(moderatorId)
                .when(securityUtil).getAuthenticatedUserId();

        Mockito.doReturn(Optional.of(moderator))
                .when(moderatorRepository).findById(moderatorId);

        moderatorService.publishNews(new NewsRequest(initialName, initialContent));

        Mockito.verify(newsRepository, Mockito.times(1)).save(Mockito.argThat((News news) -> news.getModerator().equals(moderator) || news.getName().equals(initialName) || news.getContent().equals(initialContent)));
        Mockito.verify(securityUtil, Mockito.times(1)).getAuthenticatedUserId();
    }

    @Test
    void getAllModerators() {
        Moderator moderator1 = Moderator.builder()
                .id(new UUID(42, 42))
                .username("username1")
                .build();
        Moderator moderator2 = Moderator.builder()
                .id(new UUID(42, 42))
                .username("username2")
                .build();

        Mockito.doReturn(List.of(moderator1, moderator2)).when(moderatorRepository).findAll();

        List<UserResponse> allModerators = moderatorService.getAllModerators();

        assertTrue(allModerators.containsAll(List.of(new UserResponse(moderator1), new UserResponse(moderator2))));
    }
}