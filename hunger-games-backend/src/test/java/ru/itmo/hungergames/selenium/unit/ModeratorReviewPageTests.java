package ru.itmo.hungergames.selenium.unit;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import ru.itmo.hungergames.model.entity.order.AdvertisementOrder;
import ru.itmo.hungergames.model.entity.user.Moderator;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.ApproveAdvertisingTextRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.selenium.pages.ModeratorReviewPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.ModeratorService;

@SeleniumTest
public class ModeratorReviewPageTests extends SeleniumTestBase {
    private ModeratorReviewPage page;

    @MockBean
    private ModeratorService moderatorService;

    private final AdvertisementOrder ad = AdvertisementOrder.builder()
            .id(new UUID(42, 42))
            .advertisingText("text")
            .build();

    @BeforeEach
    public void setUp() {
        final var moderator = Moderator.builder()
                .id(new UUID(42, 42))
                .username("moderator")
                .name("moderator")
                .userRoles(Set.of(UserRole.MODERATOR))
                .build();
        this.authenticate(moderator);
        this.page = PageFactory.initElements(driver, ModeratorReviewPage.class);
    }

    @Test
    public void noAd() {
        doThrow(new ResourceNotFoundException()).when(this.moderatorService).getAnotherAdvertisingText();
        this.get("/moderator/review");

        this.waitForAngularRequests();

        Assertions.assertEquals("Все посты рассмотрены", this.page.getStatus());
    }

    @Test
    public void showAd() {
        doReturn(new AdvertisingTextResponse(ad)).when(this.moderatorService).getAnotherAdvertisingText();
        this.get("/moderator/review");

        this.waitForAngularRequests();

        Assertions.assertEquals("Пост для рассмотрения", this.page.getStatus());

        Assertions.assertEquals(this.ad.getAdvertisingText(), this.page.getAdHTML());
    }

    @Test
    public void appove() {
        doReturn(new AdvertisingTextResponse(ad)).when(this.moderatorService).getAnotherAdvertisingText();
        this.get("/moderator/review");

        this.waitForAngularRequests();

        doThrow(new ResourceNotFoundException()).when(this.moderatorService).getAnotherAdvertisingText();

        this.page.approve();

        verify(this.moderatorService, times(1)).approveAdvertisingText(
            new ApproveAdvertisingTextRequest(this.ad.getId(), true));

        this.waitForAngularRequests();

        verify(this.moderatorService, times(2)).getAnotherAdvertisingText();

        Assertions.assertEquals("Все посты рассмотрены", this.page.getStatus());
    }


    @Test
    public void deny() {
        doReturn(new AdvertisingTextResponse(ad)).when(this.moderatorService).getAnotherAdvertisingText();
        this.get("/moderator/review");

        this.waitForAngularRequests();

        doThrow(new ResourceNotFoundException()).when(this.moderatorService).getAnotherAdvertisingText();

        this.page.deny();

        verify(this.moderatorService, times(1)).approveAdvertisingText(
            new ApproveAdvertisingTextRequest(this.ad.getId(), false));

        this.waitForAngularRequests();

        verify(this.moderatorService, times(2)).getAnotherAdvertisingText();

        Assertions.assertEquals("Все посты рассмотрены", this.page.getStatus());
    }
}
