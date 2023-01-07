package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.order.AdvertisementOrder;
import ru.itmo.hungergames.model.entity.Event;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.request.EventModifyRequest;
import ru.itmo.hungergames.model.request.EventRequest;
import ru.itmo.hungergames.model.entity.EventType;
import ru.itmo.hungergames.model.response.AdvertisingTextOrderResponse;
import ru.itmo.hungergames.model.response.EventResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.repository.AdvertisementOrderRepository;
import ru.itmo.hungergames.repository.EventRepository;
import ru.itmo.hungergames.repository.TributeRepository;
import ru.itmo.hungergames.service.TributeService;
import ru.itmo.hungergames.util.SecurityUtil;
import ru.itmo.hungergames.util.TributeUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TributeServiceImpl implements TributeService {
    private final TributeRepository tributeRepository;
    private final AdvertisementOrderRepository advertisementOrderRepository;
    private final EventRepository eventRepository;
    private final SecurityUtil securityUtil;
    private final TributeUtil tributeUtil;

    @Autowired
    public TributeServiceImpl(TributeRepository tributeRepository,
                              AdvertisementOrderRepository advertisementOrderRepository,
                              EventRepository eventRepository,
                              SecurityUtil securityUtil,
                              TributeUtil tributeUtil) {
        this.tributeRepository = tributeRepository;
        this.advertisementOrderRepository = advertisementOrderRepository;
        this.eventRepository = eventRepository;
        this.securityUtil = securityUtil;
        this.tributeUtil = tributeUtil;
    }

    @Override
    public TributeResponse getTributeById(UUID tributeId) {
        return new TributeResponse(tributeRepository
                .findById(tributeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Tribute with id=%s doesn't exist", tributeId))));
    }

    @Override
    public List<TributeResponse> getAllTributes() {
        return tributeRepository
                .findAll().stream()
                .map(TributeResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdvertisingTextOrderResponse sendAdvertisingText(AdvertisingTextRequest advertisingTextRequest) {
        Tribute tribute = tributeRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with such ID"));

        return new AdvertisingTextOrderResponse(
                advertisementOrderRepository.save(
                        AdvertisementOrder.builder()
                                .tribute(tribute)
                                .advertisingText(advertisingTextRequest.getText())
                                .price(BigDecimal.valueOf(200))
                                .build()));
    }

    @Override
    @Transactional
    public EventResponse addEvent(EventRequest eventRequest) {
        tributeUtil.validateEvent(eventRequest);
        Tribute tribute = tributeRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with such ID"));
        return new EventResponse(eventRepository.save(
                Event.builder()
                        .tribute(tribute)
                        .eventPlace(eventRequest.getEventPlace())
                        .eventType(eventRequest.getEventType())
                        .dateTime(eventRequest.getDateTime()).build()));
    }

    @Override
    public List<String> getEventTypes() {
        return Arrays.stream(EventType.values())
                .map(EventType::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getEvents() {
        Tribute tribute = tributeRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with such ID"));

        return eventRepository
                .findAllByTributeAndDateTimeAfter(tribute, Instant.now()).stream()
                .map(EventResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getApprovedAndPaidAdvertisingTexts() {
        return advertisementOrderRepository
                .findAllByApprovedAndPaid(true, true).stream()
                .map(advertisementOrder -> advertisementOrder.getAdvertisingText())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventResponse modifyEvent(EventModifyRequest eventModifyRequest) {
        Event event = eventRepository
                .findById(eventModifyRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no event with such ID"));

        tributeUtil.validateEventBeforeModify(eventModifyRequest);
        return new EventResponse(eventRepository.save(
                Event.builder()
                        .id(event.getId())
                        .eventPlace(eventModifyRequest.getEventPlace())
                        .eventType(eventModifyRequest.getEventType())
                        .dateTime(eventModifyRequest.getDateTime()).build()));
    }

    @Override
    @Transactional
    public void deleteEventById(UUID eventId) {
        eventRepository.deleteById(eventId);
    }
}
