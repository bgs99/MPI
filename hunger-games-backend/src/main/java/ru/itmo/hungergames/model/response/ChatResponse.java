package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.hungergames.model.entity.Chat;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatResponse {
    private UUID chatId;
    private MessageResponse lastMessage;

    public ChatResponse(Chat chat) {
        this.chatId = chat.getId();
        this.lastMessage = chat.getMessages().size() == 0 ?
                null : new MessageResponse(chat.getMessages().get(chat.getMessages().size() - 1));
    }
}
