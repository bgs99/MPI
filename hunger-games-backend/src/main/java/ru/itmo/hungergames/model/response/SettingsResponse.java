package ru.itmo.hungergames.model.response;

import lombok.*;
import ru.itmo.hungergames.model.entity.user.Settings;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettingsResponse {
    private String email;
    public SettingsResponse(Settings settings) {
        this.email = settings.getEmail();
    }
}
