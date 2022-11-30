package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.request.ChatCreateRequest;
import ru.itmo.hungergames.model.request.MessageRequest;
import ru.itmo.hungergames.model.response.ChatResponse;
import ru.itmo.hungergames.model.response.MessageResponse;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatResponse createChat(ChatCreateRequest chatCreateRequest);
    MessageResponse sendMessage(UUID chatId, MessageRequest messageRequest);
    List<MessageResponse> getMessagesByChatId(UUID chatId);
    List<ChatResponse> getChatsByUserId();
}
