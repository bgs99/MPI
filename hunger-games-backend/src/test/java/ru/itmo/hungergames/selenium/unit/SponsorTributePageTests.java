package ru.itmo.hungergames.selenium.unit;

import com.paulhammant.ngwebdriver.NgWebDriver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.itmo.hungergames.model.entity.Event;
import ru.itmo.hungergames.model.entity.EventType;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.ChatCreateRequest;
import ru.itmo.hungergames.model.response.ChatResponse;
import ru.itmo.hungergames.model.response.EventResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.selenium.pages.EventCard;
import ru.itmo.hungergames.selenium.pages.SponsorTributePage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.ChatService;
import ru.itmo.hungergames.service.TributeService;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.doReturn;
import static org.hamcrest.MatcherAssert.assertThat;

@SeleniumTest
public class SponsorTributePageTests extends SeleniumTestBase {
    private SponsorTributePage page;

    @MockBean
    private TributeService tributeService;

    @MockBean
    private ChatService chatService;

    private final int district = 3;
    private Tribute tribute;
    private final List<String> ads = List.of("text1", "text2");
    private final List<Event> events = List.of(
            Event.builder()
                    .dateTime(Instant.now().plus(Duration.ofDays(1)).truncatedTo(ChronoUnit.SECONDS))
                    .eventPlace("test place 1")
                    .eventType(EventType.INTERVIEW)
                    .build(),

            Event.builder()
                    .dateTime(Instant.now().plus(Duration.ofDays(2)).truncatedTo(ChronoUnit.SECONDS))
                    .eventPlace("test place 2")
                    .eventType(EventType.MEETING)
                    .build());

    @BeforeEach
    public void setUp() {
        this.page = PageFactory.initElements(driver, SponsorTributePage.class);

        Sponsor sponsor = Sponsor.builder()
                .id(new UUID(42, 42))
                .name("sponsor")
                .username("test")
                .password("test")
                .build();

        this.authenticate(sponsor, UserRole.SPONSOR);

        var mentor = Mentor.builder()
                .id(new UUID(42, 41))
                .district(district)
                .build();
        this.tribute = Tribute.builder()
                .id(new UUID(42, 42))
                .name("test")
                .mentor(mentor)
                .avatarUri(
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Orange_tabby_cat_sitting_on_fallen_leaves-Hisashi-01A.jpg/800px-Orange_tabby_cat_sitting_on_fallen_leaves-Hisashi-01A.jpg")
                .build();

        doReturn(new TributeResponse(tribute)).when(this.tributeService).getTributeById(tribute.getId());

        doReturn(this.ads).when(tributeService).getApprovedAndPaidAdvertisingTexts(this.tribute.getId());

        doReturn(this.events.stream().map(EventResponse::new).collect(Collectors.toList()))
                .when(this.tributeService)
                .getEvents(this.tribute.getId());

        this.get("/sponsor/tribute/" + tribute.getId());

        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver) driver);
        ngDriver.waitForAngularRequestsToFinish();
    }

    @Test
    public void correctTitle() {
        Assertions.assertEquals("Трибут " + this.tribute.getName(), this.page.getTributeTitle().getText());
    }

    @Test
    public void correctDistrict() {
        Assertions.assertEquals("Дистрикт " + this.district, this.page.getTributeDistrict().getText());
    }

    @Test
    public void correctAvatar() {
        Assertions.assertEquals(this.tribute.getAvatarUri(), this.page.getAvatar().getAttribute("src"));
    }

    @Test
    public void adsList() {
        var posts = this.page.getPosts().stream().map(WebElement::getText).collect(Collectors.toList());
        Collections.reverse(posts);

        Assertions.assertEquals(this.ads, posts);
    }

    @Test
    public void eventsList() {
        final var eventCards = this.page.getEventCards();

        final var places = eventCards.stream().map(EventCard::getPlace).collect(Collectors.toList());
        final var expectedPlaces = this.events.stream().map(Event::getEventPlace).collect(Collectors.toList());

        Assertions.assertEquals(expectedPlaces, places);

        final var dates = eventCards.stream().map(EventCard::getEventDate).collect(Collectors.toList());
        final var expectedDates = this.events.stream().map(Event::getDateTime).collect(Collectors.toList());

        Assertions.assertEquals(expectedDates, dates);

        final var eventTypes = eventCards.stream().map(EventCard::getEventType).collect(Collectors.toList());
        final var expectedEventTypes = this.events.stream().map(Event::getEventType).map(EventType::humanReadable).collect(Collectors.toList());

        Assertions.assertEquals(expectedEventTypes, eventTypes);
    }

    @Test
    public void redirectCreateOrder() {
        var sourceUrl = driver.getCurrentUrl();

        this.page.getGiveResourcesButton().click();

        this.redirectWait(sourceUrl);

        assertThat(
                driver.getCurrentUrl(),
                endsWith("#/sponsor/tribute/" + this.tribute.getId() + "/createorder"));
    }

    @Test
    public void redirectPayOrder() {
        final var sourceUrl = driver.getCurrentUrl();

        this.page.getPayResourcesButton().click();

        this.redirectWait(sourceUrl);

        assertThat(
                driver.getCurrentUrl(),
                endsWith("#/sponsor/tribute/" + this.tribute.getId() + "/payorder"));
    }

    @Test
    public void createChat() {
        doReturn(List.of()).when(this.chatService).getChatsByUserId();

        var chatCreateRequest = new ChatCreateRequest(this.tribute.getId());
        var chatResponse = ChatResponse.builder()
                .chatId(new UUID(42, 42))
                .tributeId(this.tribute.getId())
                .build();

        doReturn(chatResponse).when(this.chatService).createChat(chatCreateRequest);

        this.driver.navigate().refresh();

        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver) driver);
        ngDriver.waitForAngularRequestsToFinish();

        final var sourceUrl = driver.getCurrentUrl();

        this.page.getStartChatButton().click();

        this.redirectWait(sourceUrl);

        assertThat(
                driver.getCurrentUrl(),
                endsWith("#/sponsor/chat/" + chatResponse.getChatId()));
    }

    @Test
    public void goToChat() {
        var chatResponse = ChatResponse.builder()
                .chatId(new UUID(42, 42))
                .tributeId(this.tribute.getId())
                .tributeName(this.tribute.getName())
                .build();
        doReturn(List.of(chatResponse)).when(this.chatService).getChatsByUserId();

        this.driver.navigate().refresh();

        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver) driver);
        ngDriver.waitForAngularRequestsToFinish();

        final var sourceUrl = driver.getCurrentUrl();

        this.page.getGoToChatButton().click();

        this.redirectWait(sourceUrl);

        assertThat(
                driver.getCurrentUrl(),
                endsWith("#/sponsor/chat/" + chatResponse.getChatId()));
    }
}
