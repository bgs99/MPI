package ru.itmo.hungergames.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tribute_id", nullable = false)
    @JsonBackReference
    private Tribute tribute;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    @JsonBackReference
    private Sponsor sponsor;

    @OneToMany(
            mappedBy = "chat",
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<Message> messages;
}
