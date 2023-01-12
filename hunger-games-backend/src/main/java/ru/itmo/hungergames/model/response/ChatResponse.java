package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.hungergames.model.entity.chat.Chat;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ChatResponse {
    private UUID chatId;
    private MessageResponse lastMessage;
    private String mentorName;
    private String tributeName;
    private String sponsorName;
    private UUID tributeId;

    public ChatResponse(Chat chat) {
        this.chatId = chat.getId();
        this.lastMessage = chat.getMessages().size() == 0 ?
                null : new MessageResponse(chat.getMessages().get(chat.getMessages().size() - 1));
        this.mentorName = chat.getTribute().getMentor().getName();
        this.tributeName = chat.getTribute().getName();
        this.sponsorName = chat.getSponsor().getName();
        this.tributeId = chat.getTribute().getId();
    }
}
