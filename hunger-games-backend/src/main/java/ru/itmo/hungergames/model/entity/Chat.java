package ru.itmo.hungergames.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @Column(name = "message_id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;

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
