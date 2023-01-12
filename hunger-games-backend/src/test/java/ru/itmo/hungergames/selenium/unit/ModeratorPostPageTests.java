package ru.itmo.hungergames.selenium.unit;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.itmo.hungergames.model.entity.user.Moderator;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.NewsRequest;
import ru.itmo.hungergames.selenium.pages.ModeratorPostPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.ModeratorService;

@SeleniumTest
public class ModeratorPostPageTests extends SeleniumTestBase {
    private ModeratorPostPage page;

    @MockBean
    private ModeratorService moderatorService;

    @BeforeEach
    public void setUp() {
        final var moderator = Moderator.builder()
                .id(new UUID(42, 42))
                .username("moderator")
                .name("moderator")
                .userRoles(Set.of(UserRole.MODERATOR))
                .build();
        this.authenticate(moderator);
        this.get("/moderator/post");
        this.page = PageFactory.initElements(driver, ModeratorPostPage.class);
    }

    @Test
    public void publishNews() {
        final var title = "Title";
        final var content = "content";
        final var expectedRequest = NewsRequest.builder()
                .name(title)
                .content(content)
                .build();
        doNothing().when(this.moderatorService).publishNews(expectedRequest);

        this.page.getTitleInput().sendKeys(title);

        this.page.getPostEditorElement().sendKeys(content);

        this.page.getPublishButton().click();

        this.waitForAngularRequests();

        verify(this.moderatorService, times(1)).publishNews(expectedRequest);
    }
}
