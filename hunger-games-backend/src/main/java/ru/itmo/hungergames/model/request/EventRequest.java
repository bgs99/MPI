package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.hungergames.model.entity.EventType;
import ru.itmo.hungergames.util.annotation.JsonLocalDateTime;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    @JsonLocalDateTime
    @NotNull
    private LocalDateTime dateTime;
    @NotNull
    private EventType eventType;
    @NotBlank(message = "Event place should not be blank")
    private String eventPlace;
}
