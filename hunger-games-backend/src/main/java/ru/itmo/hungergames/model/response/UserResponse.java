package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.user.User;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class UserResponse {
    private UUID id;
    private String name;
    private String username;
    private String avatarUri;
    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.avatarUri = user.getAvatarUri();
    }
}
