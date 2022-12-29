package ru.itmo.hungergames.util;

import org.springframework.stereotype.Component;
import ru.itmo.hungergames.exception.NotCorrectDateException;
import ru.itmo.hungergames.model.request.EventModifyRequest;
import ru.itmo.hungergames.model.request.EventRequest;

import java.time.LocalDateTime;

@Component
public class TributeUtil {
    public void validateEvent(EventRequest eventRequest) {
        if (eventRequest.getDateTime().isBefore(LocalDateTime.now())) {
            throw new NotCorrectDateException(
                    String.format("%s is before current time when it should be after", eventRequest.getDateTime()));
        }
    }

    public void validateEventBeforeModify(EventModifyRequest eventModifyRequest) {
        if (eventModifyRequest.getDateTime().isBefore(LocalDateTime.now())) {
            throw new NotCorrectDateException(
                    String.format("%s is before current time when it should be after", eventModifyRequest.getDateTime()));
        }
    }
}
