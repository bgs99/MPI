package ru.itmo.hungergames.model.entity.chat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.itmo.hungergames.model.entity.chat.Chat;
import ru.itmo.hungergames.model.entity.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @Column(name = "message_id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;
    @NotNull
    private String message;
    @NotNull
    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "chat_id")
    @NotNull
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
}
