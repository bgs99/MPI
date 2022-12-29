package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.hungergames.model.entity.Event;
import ru.itmo.hungergames.model.entity.EventType;
import ru.itmo.hungergames.util.annotation.JsonLocalDateTime;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EventResponse {
    private UUID id;
    @JsonLocalDateTime
    private LocalDateTime dateTime;
    private String eventPlace;
    private EventType eventType;

    public EventResponse(Event event) {
        this.id = event.getId();
        this.eventType = event.getEventType();
        this.eventPlace = event.getEventPlace();
        this.dateTime = event.getDateTime();
    }
}
