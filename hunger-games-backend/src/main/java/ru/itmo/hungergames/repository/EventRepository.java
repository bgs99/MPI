package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.hungergames.model.entity.Event;
import ru.itmo.hungergames.model.entity.user.Tribute;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findAllByTributeAndDateTimeAfter(Tribute tribute, Instant dateTime);
}
