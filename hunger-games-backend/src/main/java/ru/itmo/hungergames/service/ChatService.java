package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.request.ChatCreateRequest;
import ru.itmo.hungergames.model.request.MessageRequest;
import ru.itmo.hungergames.model.response.ChatResponse;
import ru.itmo.hungergames.model.response.MessageResponse;

import java.util.List;

public interface ChatService {
    ChatResponse createChat(ChatCreateRequest chatCreateRequest);
    MessageResponse sendMessage(MessageRequest messageRequest);
    List<MessageResponse> getMessagesByChatId(Long chatId);
    List<ChatResponse> getChatsByUserId();
}
