package ru.itmo.hungergames.selenium.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.Event;
import ru.itmo.hungergames.model.entity.EventType;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.EventRequest;
import ru.itmo.hungergames.model.response.EventResponse;
import ru.itmo.hungergames.selenium.pages.TributeEventsPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.TributeService;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SeleniumTest
public class TributeEventsPageTests extends SeleniumTestBase {

    private TributeEventsPage page;
    private List<Event> events;
    private List<Instant> expectedDates;

    private final String cardDatePattern = "dd.MM.uuuu, HH:mm:ss";
    private final String formDatePattern = "M/dd/uuuu, HH:mm:ss";

    @MockBean
    TributeService tributeService;

    private final Tribute self = Tribute.builder()
            .id(new UUID(42, 42))
            .name("tribute")
            .username("test")
            .password("test")
            .userRoles(Set.of(UserRole.TRIBUTE))
            .build();

    @BeforeEach
    public void SetUp() {
        this.page = new TributeEventsPage(this.driver);
        this.authenticate(this.self, UserRole.TRIBUTE);

        expectedDates = List.of(
                Instant.now().plus(Duration.ofDays(1)).truncatedTo(ChronoUnit.SECONDS),
                Instant.now().plus(Duration.ofDays(2)).truncatedTo(ChronoUnit.SECONDS),
                Instant.now().plus(Duration.ofDays(3)).truncatedTo(ChronoUnit.SECONDS)
        );

        Event event1 = Event.builder()
                .id(new UUID(42, 42))
                .tribute(self)
                .eventPlace("place1")
                .eventType(EventType.INTERVIEW)
                .dateTime(expectedDates.get(0))
                .build();
        Event event2 = Event.builder()
                .id(new UUID(42, 43))
                .tribute(self)
                .eventPlace("place2")
                .eventType(EventType.MEETING)
                .dateTime(expectedDates.get(1))
                .build();
        Event event3 = Event.builder()
                .id(new UUID(42, 44))
                .tribute(self)
                .eventPlace("place3")
                .eventType(EventType.INTERVIEW)
                .dateTime(expectedDates.get(2))
                .build();

        events = new ArrayList<>();

        events.add(event1);
        events.add(event2);
        events.add(event3);

        clearInvocations(tributeService);
        doReturn(events.stream().map(EventResponse::new).collect(Collectors.toList()))
                .when(tributeService)
                .getEvents();
    }

    private Instant instantFromString(String stringDate, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate, dateTimeFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }

    @Test
    public void showEventList() {
        this.get("/tribute/events");

        page.waitUntilEventsLoaded(events.size());

        var eventCards = page.getEventCards();

        var places = eventCards.stream().map(TributeEventsPage.EventCard::getPlace).collect(Collectors.toList());
        var expectedPlaced = List.of("place1", "place2", "place3");

        Assertions.assertEquals(expectedPlaced, places);

        var dates = eventCards.stream().map(TributeEventsPage.EventCard::getEventDate).collect(Collectors.toList());

        Assertions.assertEquals(expectedDates, dates);

        var eventTypes = eventCards.stream().map(TributeEventsPage.EventCard::getEventType).collect(Collectors.toList());
        var expectedEventTypes = List.of("Интервью", "Встреча", "Интервью");

        Assertions.assertEquals(expectedEventTypes, eventTypes);
    }

    @Test
    public void showNewEventForm() {
        this.get("/tribute/events");
        page.getAddEventButton().click();
        var radioButtons = page.getRadioButtons();

        Assertions.assertTrue(page.getApproveButton().isDisplayed());
        Assertions.assertTrue(page.getCancelButton().isDisplayed());

        Assertions.assertTrue(page.getDateInput().isDisplayed());
        Assertions.assertEquals("", page.getDateInput().getAttribute("value"));

        Assertions.assertTrue(page.getPlaceInput().isDisplayed());
        Assertions.assertEquals("", page.getPlaceInput().getAttribute("value"));

        Assertions.assertEquals(2, radioButtons.size());
        Assertions.assertFalse(radioButtons.get(0).isSelected() || radioButtons.get(0).isSelected());
    }

    @Test
    public void showEditEventForm() {
        this.get("/tribute/events");
        page.waitUntilEventsLoaded(events.size());
        var eventCards = page.getEventCards();

        eventCards.get(0).getEditButton().click();

        List<WebElement> radioButtons = page.getRadioButtons();

        Assertions.assertTrue(page.getApproveButton().isDisplayed());
        Assertions.assertTrue(page.getCancelButton().isDisplayed());

        Assertions.assertTrue(radioButtons.get(0).isSelected() || radioButtons.get(1).isSelected());
        Assertions.assertEquals("place1", page.getPlaceInput().getAttribute("value"));
        Assertions.assertEquals(expectedDates.get(0), instantFromString(page.getDateInput().getAttribute("value"), formDatePattern));
    }

    @Test
    public void addNewEvent() {
        String expectedDate = "25.01.2142, 22:27:13";
        String expectedPlace = "here";

        Event event = Event.builder()
                .eventPlace(expectedPlace)
                .eventType(EventType.INTERVIEW)
                .dateTime(instantFromString(expectedDate, cardDatePattern))
                .tribute(self).build();

        var expectedEventRequest = new EventRequest(instantFromString(expectedDate, cardDatePattern), EventType.INTERVIEW, expectedPlace);

        EventResponse eventResponse = new EventResponse(event);

        clearInvocations(tributeService);

        doReturn(List.of(eventResponse))
                .when(tributeService)
                .getEvents();

        doReturn(eventResponse)
                .when(tributeService).addEvent(any());

        this.get("/tribute/events");
        page.getAddEventButton().click();
        page.enterDate("1/25/2142, 22:27:13");
        page.enterPlace(expectedPlace);

        List<WebElement> radioButtons = page.getRadioButtons();

        radioButtons.get(0).click();

        page.getApproveButton().click();

        verify(tributeService, times(1)).addEvent(any());

        page.waitUntilEventsLoaded(1);

        var eventCard = page.getEventCards().get(0);

        Assertions.assertEquals(expectedDate, eventCard.getEventDateString());
        Assertions.assertEquals("Интервью", eventCard.getEventType());
        Assertions.assertEquals(expectedPlace, eventCard.getPlace());
    }

    @Test
    public void cancelChanges() {
        this.get("/tribute/events");
        page.waitUntilEventsLoaded(events.size());

        var eventCards = page.getEventCards();

        var places = eventCards.stream().map(TributeEventsPage.EventCard::getPlace).collect(Collectors.toList());
        var expectedPlaced = List.of("place1", "place2", "place3");

        var dates = eventCards.stream().map(TributeEventsPage.EventCard::getEventDate).collect(Collectors.toList());

        var eventTypes = eventCards.stream().map(TributeEventsPage.EventCard::getEventType).collect(Collectors.toList());
        var expectedEventTypes = List.of("Интервью", "Встреча", "Интервью");

        eventCards.get(0).getEditButton().click();

        page.enterDate("1/25/2142, 22:27:13");
        page.enterPlace("new place");
        List<WebElement> radioButtons = page.getRadioButtons();
        if (radioButtons.get(0).isSelected())
            radioButtons.get(1).click();
        else radioButtons.get(0).click();

        page.getCancelButton().click();

        Assertions.assertEquals(expectedPlaced, places);
        Assertions.assertEquals(expectedDates, dates);
        Assertions.assertEquals(expectedEventTypes, eventTypes);
    }

    @Test
    public void cancelAddNewEvent() {
        this.get("/tribute/events");
        page.waitUntilEventsLoaded(events.size());

        page.getAddEventButton().click();
        page.enterDate("1/25/2142, 22:27:13");
        page.enterPlace("new place");

        List<WebElement> radioButtons = page.getRadioButtons();

        radioButtons.get(0).click();

        page.getCancelButton().click();

        verify(tributeService, times(0)).addEvent(any());

        page.waitUntilEventsLoaded();

        Assertions.assertEquals(events.size(), page.getEventCards().size());
    }

    @Test
    public void deleteEvent() {
        this.get("/tribute/events");

        int startSize = events.size();
        page.waitUntilEventsLoaded(startSize);

        clearInvocations(tributeService);
        events.remove(0);
        doReturn(events).when(tributeService).getEvents();

        page.getEventCards().get(0).getDeleteButton().click();

        verify(tributeService, times(1)).deleteEventById(any());

        page.waitUntilEventsLoaded(startSize - 1);

        Assertions.assertEquals(startSize - 1, page.getEventCards().size());
    }

    @Test
    public void enterInvalidData() {
        this.get("/tribute/events");
        page.waitUntilEventsLoaded(events.size());

        page.getAddEventButton().click();

        page.getApproveButton().click();

        verify(tributeService, times(0)).addEvent(any());

        Assertions.assertTrue(page.getApproveButton().isDisplayed());
    }

}
