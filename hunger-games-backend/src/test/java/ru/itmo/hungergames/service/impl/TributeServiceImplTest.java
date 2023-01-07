package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.exception.NotCorrectDateException;
import ru.itmo.hungergames.model.entity.Event;
import ru.itmo.hungergames.model.entity.EventType;
import ru.itmo.hungergames.model.entity.order.AdvertisementOrder;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.request.EventModifyRequest;
import ru.itmo.hungergames.model.request.EventRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextOrderResponse;
import ru.itmo.hungergames.model.response.EventResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.repository.AdvertisementOrderRepository;
import ru.itmo.hungergames.repository.EventRepository;
import ru.itmo.hungergames.repository.TributeRepository;
import ru.itmo.hungergames.service.TributeService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TributeServiceImplTest {
    @Autowired
    TributeService tributeService;
    @MockBean
    AdvertisementOrderRepository advertisementOrderRepository;
    @MockBean
    TributeRepository tributeRepository;
    @MockBean
    SecurityUtil securityUtil;
    @MockBean
    EventRepository eventRepository;

    @Test
    void getTributeById() {
        Tribute tribute = new Tribute();
        UUID id = UUID.randomUUID();
        tribute.setName("name");
        tribute.setId(id);
        Mockito.doReturn(Optional.of(tribute))
                .when(tributeRepository)
                .findById(id);

        TributeResponse tributeById = tributeService.getTributeById(id);

        assertEquals("name", tributeById.getName());
        assertEquals(id, tributeById.getId());
        Mockito.verify(tributeRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void getTributeNotExists() {
        UUID id = UUID.randomUUID();
        Throwable thrown = catchThrowable(() -> tributeService.getTributeById(id));
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).contains(String.format("Tribute with id=%s doesn't exist", id));
    }

    @Test
    void getAllTributes() {
        Tribute tribute = new Tribute();
        tribute.setId(new UUID(42, 42));

        Mockito.when(tributeRepository.findAll()).thenReturn(List.of(tribute));

        List<TributeResponse> tributeResponses = tributeService.getAllTributes();
        List<UUID> uuids = tributeResponses.stream().map(TributeResponse::getId).toList();

        assertEquals(uuids, List.of(tribute.getId()));
        Mockito.verify(tributeRepository, Mockito.times(1)).findAll();
    }

    @Test
    void sendAdvertisingText() {
        Tribute tribute = new Tribute();
        tribute.setId(new UUID(42, 42));

        Mockito.doReturn(Optional.of(tribute))
                .when(tributeRepository)
                .findById(tribute.getId());
        Mockito.doReturn(tribute.getId())
                .when(securityUtil)
                .getAuthenticatedUserId();
        Mockito.doReturn(tribute)
                .when(securityUtil)
                .getAuthenticatedUser();

        var adText = "Some Text";

        var saveAd = AdvertisementOrder.builder()
                .tribute(tribute)
                .advertisingText(adText)
                .price(BigDecimal.valueOf(200))
                .build();

        var ad = saveAd.toBuilder()
                .id(new UUID(42, 42))
                .build();

        Mockito.when(advertisementOrderRepository.save(saveAd)).thenReturn(ad);

        AdvertisingTextOrderResponse advertisingTextOrderResponse = tributeService.sendAdvertisingText(new AdvertisingTextRequest("Some Text"));

        Mockito.verify(advertisementOrderRepository, Mockito.times(1)).save(saveAd);

        assertEquals(advertisingTextOrderResponse.getPrice(), ad.getPrice());
    }

    @Test
    void addEvent() {
        Tribute tribute = new Tribute();
        tribute.setId(new UUID(42, 42));
        String place = "some place";

        var eventTime = Instant.now().plus(Duration.ofDays(1));

        Event saveEvent = Event.builder()
                .eventType(EventType.INTERVIEW)
                .eventPlace(place)
                .dateTime(eventTime)
                .tribute(tribute)
                .build();

        Event event = saveEvent.toBuilder()
                .id(new UUID(42, 42))
                .build();

        Mockito.when(tributeRepository.findById(tribute.getId())).thenReturn(Optional.of(tribute));
        Mockito.when(eventRepository.save(saveEvent)).thenReturn(event);

        Mockito.doReturn(tribute.getId())
                .when(securityUtil)
                .getAuthenticatedUserId();

        EventRequest eventRequest = new EventRequest(eventTime, EventType.INTERVIEW, place);
        EventResponse eventResponse = tributeService.addEvent(eventRequest);

        Mockito.verify(eventRepository, Mockito.times(1)).save(saveEvent);

        Mockito.clearInvocations(eventRepository);

        assertEquals(place, eventResponse.getEventPlace());
        assertEquals(EventType.INTERVIEW, eventResponse.getEventType());
    }

    @Test
    void validateEventFailure() {
        Tribute tribute = new Tribute();
        tribute.setId(new UUID(42, 42));

        Mockito.when(tributeRepository.findById(tribute.getId())).thenReturn(Optional.of(tribute));

        Mockito.doReturn(tribute.getId())
                .when(securityUtil)
                .getAuthenticatedUserId();

        Mockito.doReturn(tribute.getId())
                .when(securityUtil)
                .getAuthenticatedUserId();

        EventRequest eventRequest = new EventRequest(Instant.now().minus(30, ChronoUnit.DAYS), EventType.INTERVIEW, "some place");
        Throwable thrown = catchThrowable(() -> tributeService.addEvent(eventRequest));

        assertThat(thrown).isInstanceOf(NotCorrectDateException.class);
        assertNotNull(thrown.getMessage());
    }

    @Test
    void getEvents() {
        Tribute tribute = new Tribute();
        tribute.setId(new UUID(42, 42));

        Event futureEvent = Event.builder()
                .id(new UUID(42, 43))
                .tribute(tribute)
                .dateTime(Instant.now().plus(Duration.ofDays(1)))
                .build();

        Mockito.when(tributeRepository.findById(tribute.getId())).thenReturn(Optional.of(tribute));

        Mockito.when(eventRepository.findAllByTributeAndDateTimeAfter(Mockito.eq(tribute), Mockito.any()))
                        .thenReturn(List.of(futureEvent));

        Mockito.doReturn(tribute.getId())
                .when(securityUtil)
                .getAuthenticatedUserId();

        List<EventResponse> events = tributeService.getEvents();
        List<UUID> eventUUIDs = events.stream().map(EventResponse::getId).toList();

        assertEquals(eventUUIDs, List.of(futureEvent.getId()));
    }

    @Test
    void modifyEvent() {
        Tribute tribute = new Tribute();
        tribute.setId(new UUID(42, 42));
        String place = "some place";

        var eventTime = Instant.now().plus(Duration.ofDays(1));

        Event event = Event.builder()
                .id(new UUID(42, 42))
                .eventType(EventType.MEETING)
                .eventPlace(place)
                .dateTime(eventTime)
                .tribute(tribute)
                .build();

        Mockito.when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        EventModifyRequest eventModifyRequest = new EventModifyRequest(event.getId(), eventTime, EventType.MEETING, place);
        EventResponse eventResponse = tributeService.modifyEvent(eventModifyRequest);

        Mockito.verify(eventRepository, Mockito.times(1)).save(event);
        Mockito.clearInvocations(eventRepository);

        assertEquals(place, eventResponse.getEventPlace());
        assertEquals(EventType.MEETING, eventResponse.getEventType());
        assertEquals(event.getId(), eventResponse.getId());
    }

    @Test
    void noSuchEvent() {
        Tribute tribute = new Tribute();
        tribute.setId(new UUID(42, 42));


        Mockito.when(tributeRepository.findById(tribute.getId())).thenReturn(Optional.of(tribute));
        Mockito.doReturn(tribute.getId())
                .when(securityUtil)
                .getAuthenticatedUserId();

        UUID eventUUID = new UUID(1, 1);
        String place = "some place";

        EventModifyRequest eventModifyRequest = new EventModifyRequest(eventUUID, Instant.now().plus(30, ChronoUnit.MINUTES), EventType.MEETING, place);
        Throwable thrown = catchThrowable(() -> tributeService.modifyEvent(eventModifyRequest));

        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertNotNull(thrown.getMessage());
    }

    @Test
    void validateBeforeModifyFailure() {
        Tribute tribute = new Tribute();
        tribute.setId(new UUID(42, 42));

        var eventTime = Instant.now().plus(Duration.ofDays(1));
        var eventPlace = "some place";

        Event event = Event.builder()
                .id(new UUID(42, 42))
                .eventType(EventType.MEETING)
                .eventPlace(eventPlace)
                .dateTime(eventTime)
                .tribute(tribute)
                .build();

        Mockito.when(tributeRepository.findById(tribute.getId())).thenReturn(Optional.of(tribute));
        Mockito.when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        Mockito.doReturn(tribute.getId())
                .when(securityUtil)
                .getAuthenticatedUserId();

        EventModifyRequest eventModifyRequest = new EventModifyRequest(event.getId(), Instant.now().minus(30, ChronoUnit.MINUTES), EventType.MEETING, eventPlace);

        Throwable thrown = catchThrowable(() -> tributeService.modifyEvent(eventModifyRequest));

        assertThat(thrown).isInstanceOf(NotCorrectDateException.class);
        assertNotNull(thrown.getMessage());
    }

}