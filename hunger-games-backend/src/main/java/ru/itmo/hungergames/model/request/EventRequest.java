package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.hungergames.model.entity.EventType;
import ru.itmo.hungergames.util.annotation.JsonDateTime;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    @JsonDateTime
    @NotNull
    private Instant dateTime;
    @NotNull
    private EventType eventType;
    @NotBlank(message = "Event place should not be blank")
    private String eventPlace;
}
