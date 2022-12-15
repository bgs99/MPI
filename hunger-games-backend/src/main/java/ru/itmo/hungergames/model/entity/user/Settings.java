package ru.itmo.hungergames.model.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "settings")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Settings {
    @Id
    @Column(name = "setting_id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;
    @Email
    private String email;
}
