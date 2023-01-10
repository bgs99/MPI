package ru.itmo.hungergames.selenium.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.News;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.response.NewsResponse;
import ru.itmo.hungergames.selenium.pages.SponsorNewsPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.SponsorService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.doReturn;

@SeleniumTest
public class SponsorNewsPageTests extends SeleniumTestBase {
    private SponsorNewsPage page;

    @MockBean
    private SponsorService sponsorService;

    private final List<News> news = List.of(
            News.builder()
                    .content("abc")
                    .build(),
            News.builder()
                    .content("<b>abc</b>")
                    .build()
    );

    @BeforeEach
    public void setUp() {
        var sponsor = User.builder()
                .id(new UUID(42, 42))
                .username("sponsor")
                .name("sponsor")
                .build();
        this.authenticate(sponsor, UserRole.SPONSOR);

        doReturn(this.news.stream().map(NewsResponse::new).collect(Collectors.toList())).when(this.sponsorService).getNews();

        this.get("/sponsor/news");
        this.page = new SponsorNewsPage(this.driver);
        PageFactory.initElements(this.driver, this.page);

        this.waitForAngularRequests();
    }

    @Test
    public void simpleText() {
        Assertions.assertEquals(this.news.get(0).getContent(), this.page.getEntries().get(0).getContents());
    }

    @Test
    public void richText() {
        Assertions.assertEquals(this.news.get(1).getContent(), this.page.getEntries().get(1).getContents());
    }
}
