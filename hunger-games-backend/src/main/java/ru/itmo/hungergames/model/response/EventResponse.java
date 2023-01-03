package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.hungergames.model.entity.Event;
import ru.itmo.hungergames.model.entity.EventType;
import ru.itmo.hungergames.util.annotation.JsonDateTime;

import java.time.Instant;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EventResponse {
    private UUID id;
    @JsonDateTime
    private Instant dateTime;
    private String eventPlace;
    private EventType eventType;

    public EventResponse(Event event) {
        this.id = event.getId();
        this.eventType = event.getEventType();
        this.eventPlace = event.getEventPlace();
        this.dateTime = event.getDateTime();
    }
}
