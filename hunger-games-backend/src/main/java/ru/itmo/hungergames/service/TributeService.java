package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.request.EventModifyRequest;
import ru.itmo.hungergames.model.request.EventRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextOrderResponse;
import ru.itmo.hungergames.model.response.EventResponse;
import ru.itmo.hungergames.model.response.TributeResponse;

import java.util.List;
import java.util.UUID;

public interface TributeService {
    TributeResponse getTributeById(UUID tribute);
    List<TributeResponse> getAllTributes();
    AdvertisingTextOrderResponse sendAdvertisingText(AdvertisingTextRequest advertisingTextRequest);

    EventResponse addEvent(EventRequest eventRequest);

    List<String> getEventTypes();

    List<EventResponse> getEvents();

    List<String> getApprovedAndPaidAdvertisingTexts();

    EventResponse modifyEvent(EventModifyRequest eventModifyRequest);

    void deleteEventById(UUID eventId);
}
