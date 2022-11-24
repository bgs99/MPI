package ru.itmo.hungergames.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@Builder
public class Message {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;
    private String message;
    private LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
