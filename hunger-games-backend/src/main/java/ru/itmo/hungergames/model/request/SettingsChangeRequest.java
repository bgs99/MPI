package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SettingsChangeRequest {
    @Email
    @NotNull
    private String email;
}
