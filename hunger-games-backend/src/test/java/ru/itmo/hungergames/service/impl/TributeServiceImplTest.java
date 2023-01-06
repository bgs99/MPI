package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.jdbc.Sql;
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
import ru.itmo.hungergames.util.TributeUtil;

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
    @Autowired
    AdvertisementOrderRepository advertisementOrderRepository;
    @SpyBean
    TributeRepository tributeRepository;
    @MockBean
    SecurityUtil securityUtil;
    @SpyBean
    EventRepository eventRepository;
    @SpyBean
    TributeUtil tributeUtil;

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
    @Sql(value = {"/initScripts/create-multiple-tributes.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-multiple-tributes.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllTributes() {
        List<UUID> initialUUIDs = new ArrayList<>();
        initialUUIDs.add(UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32"));
        initialUUIDs.add(UUID.fromString("c0b91cca-27ba-49d2-85e1-290cbd73d45e"));
        initialUUIDs.add(UUID.fromString("3ee25464-bdec-4237-a335-94c1f376e8d6"));

        List<TributeResponse> tributeResponses = tributeService.getAllTributes();
        List<UUID> uuids = tributeResponses.stream().map(TributeResponse::getId).toList();

        assertTrue(uuids.containsAll(initialUUIDs));
        Mockito.verify(tributeRepository, Mockito.times(1)).findAll();
    }

    @Test
    @Sql(value = {"/initScripts/create-tribute.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-tribute.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendAdvertisingText() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Tribute tribute = new Tribute();
        tribute.setId(tributeId);

        Mockito.doReturn(Optional.of(tribute))
                .when(tributeRepository)
                .findById(tributeId);
        Mockito.doReturn(tributeId)
                .when(securityUtil)
                .getAuthenticatedUserId();
        Mockito.doReturn(tribute)
                .when(securityUtil)
                .getAuthenticatedUser();

        AdvertisingTextOrderResponse advertisingTextOrderResponse = tributeService.sendAdvertisingText(new AdvertisingTextRequest("Some Text"));
        AdvertisementOrder advertisementOrder = advertisementOrderRepository.findById(advertisingTextOrderResponse.getOrderId()).orElseThrow();

        assertEquals("Some Text", advertisementOrder.getAdvertisingText());
        assertNotNull(advertisementOrder.getPrice());
        assertNull(advertisementOrder.getApproved());
        assertFalse(advertisementOrder.isPaid());
        advertisementOrderRepository.delete(advertisementOrder);
    }

    @Test
    @Sql(value = {"/initScripts/create-tribute.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-tribute.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addEvent() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        String place = "some place";

        Mockito.doReturn(tributeId)
                .when(securityUtil)
                .getAuthenticatedUserId();

        EventRequest eventRequest = new EventRequest(Instant.now().plus(30, ChronoUnit.DAYS), EventType.INTERVIEW, place);
        EventResponse eventResponse = tributeService.addEvent(eventRequest);
        Event eventFromDb = eventRepository.findById(eventResponse.getId()).orElseThrow();

        Mockito.verify(eventRepository, Mockito.times(1)).save(ArgumentMatchers.any(Event.class));
        Mockito.clearInvocations(eventRepository);

        assertEquals(place, eventFromDb.getEventPlace());
        assertEquals(EventType.INTERVIEW, eventFromDb.getEventType());
        assertEquals(tributeId, eventFromDb.getTribute().getId());
        assertEquals(place, eventResponse.getEventPlace());
        assertEquals(EventType.INTERVIEW, eventResponse.getEventType());

        eventRepository.delete(eventFromDb);
    }

    @Test
    @Sql(value = {"/initScripts/create-tribute.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-tribute.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void validateEventFailure() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        String place = "some place";

        Mockito.doReturn(tributeId)
                .when(securityUtil)
                .getAuthenticatedUserId();

        EventRequest eventRequest = new EventRequest(Instant.now().minus(30, ChronoUnit.DAYS), EventType.INTERVIEW, place);
        Throwable thrown = catchThrowable(() -> tributeService.addEvent(eventRequest));

        assertThat(thrown).isInstanceOf(NotCorrectDateException.class);
        assertNotNull(thrown.getMessage());
    }

    @Test
    @Sql(value = {"/initScripts/create-tributes-with-events.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-tributes-with-events.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getEvents() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Mockito.doReturn(tributeId)
                .when(securityUtil)
                .getAuthenticatedUserId();

        List<UUID> futureEventsUUIDs = List.of(
                UUID.fromString("6a2a8ab8-4bd8-400f-bd7b-306ede0bb39a"),
                UUID.fromString("78503b7f-8444-4f04-b927-79391199757e")
        );
        UUID expiredEventUUID = UUID.fromString("52a2febf-feb1-485c-8641-d7da61b767e9");
        List<UUID> eventsOfOtherTributesUUIDs = List.of(
                UUID.fromString("b97d1545-e96e-4e86-8711-5ae28bdba97a"),
                UUID.fromString("39e19181-e20e-4c6a-b6d4-10ada8891ecd"),
                UUID.fromString("d5997e85-aaec-4af5-8c57-b0c4f4f81072"),
                UUID.fromString("c4bab111-2c52-4ae3-9145-1fdb98400e2b"),
                UUID.fromString("fc2da925-046c-413a-a129-e72a9683c078"),
                UUID.fromString("943d0959-1394-4f96-b7fe-5281ec093716")
        );

        List<EventResponse> events = tributeService.getEvents();
        List<UUID> eventUUIDs = events.stream().map(EventResponse::getId).toList();

        assertTrue(eventUUIDs.containsAll(futureEventsUUIDs));
        assertTrue(Collections.disjoint(eventUUIDs, eventsOfOtherTributesUUIDs));
        assertFalse(eventUUIDs.contains(expiredEventUUID));
        List<Instant> instantList = events.stream().map(EventResponse::getDateTime).filter(instant -> (!instant.isAfter(Instant.now()))).toList();
        assertTrue(instantList.isEmpty());
    }

    @Test
    @Sql(value = {"/initScripts/create-tributes-with-events.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-tributes-with-events.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void modifyEvent() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Mockito.doReturn(tributeId)
                .when(securityUtil)
                .getAuthenticatedUserId();
        UUID eventUUID = UUID.fromString("6a2a8ab8-4bd8-400f-bd7b-306ede0bb39a");
        String place = "new place";

        EventModifyRequest eventModifyRequest = new EventModifyRequest(eventUUID, Instant.now().plus(30, ChronoUnit.MINUTES), EventType.MEETING, place);
        EventResponse eventResponse = tributeService.modifyEvent(eventModifyRequest);

        Mockito.verify(eventRepository, Mockito.times(1)).save(ArgumentMatchers.any(Event.class));
        Mockito.clearInvocations(eventRepository);

        assertEquals(place, eventResponse.getEventPlace());
        assertEquals(EventType.MEETING, eventResponse.getEventType());
        assertEquals(eventUUID, eventResponse.getId());
    }

    @Test
    @Sql(value = {"/initScripts/create-tributes-with-events.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-tributes-with-events.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void noSuchEvent() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Mockito.doReturn(tributeId)
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
    @Sql(value = {"/initScripts/create-tributes-with-events.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-tributes-with-events.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void validateBeforeModifyFailure() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Mockito.doReturn(tributeId)
                .when(securityUtil)
                .getAuthenticatedUserId();
        UUID eventUUID = UUID.fromString("6a2a8ab8-4bd8-400f-bd7b-306ede0bb39a");
        String place = "some place";

        EventModifyRequest eventModifyRequest = new EventModifyRequest(eventUUID, Instant.now().minus(30, ChronoUnit.MINUTES), EventType.MEETING, place);
        Throwable thrown = catchThrowable(() -> tributeService.modifyEvent(eventModifyRequest));

        assertThat(thrown).isInstanceOf(NotCorrectDateException.class);
        assertNotNull(thrown.getMessage());
    }

}