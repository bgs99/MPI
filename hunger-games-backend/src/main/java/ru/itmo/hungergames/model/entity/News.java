package ru.itmo.hungergames.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.itmo.hungergames.model.entity.user.Moderator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "news")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class News {
    @Id
    @Column(name = "message_id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String content;
    @NotNull
    @ManyToOne
    private Moderator moderator;
    @NotNull
    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.now();
}
