package ru.itmo.hungergames.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.itmo.hungergames.model.entity.user.Tribute;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "event_id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String eventPlace;
    private LocalDateTime dateTime;
    @OneToOne
    @JoinColumn(name = "tribute_id")
    private Tribute tribute;
}