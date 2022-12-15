package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.hungergames.model.entity.chat.Message;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.util.annotation.JsonLocalDateTime;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private UUID id;
    private String role;
    @JsonLocalDateTime
    private LocalDateTime dateTime;
    private String message;
    private String senderName;
    private String senderAvatarUri;

    public MessageResponse(Message message) {
        User sender = message.getUser();
        this.id = message.getId();
        this.role = sender.getUserRoles().iterator().next().name();
        this.dateTime = message.getDateTime();
        this.message = message.getMessage();
        this.senderName = sender.getName();
        this.senderAvatarUri = sender.getAvatarUri();
    }
}
