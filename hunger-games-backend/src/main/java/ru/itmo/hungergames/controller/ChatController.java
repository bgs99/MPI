package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.request.ChatCreateRequest;
import ru.itmo.hungergames.model.request.MessageRequest;
import ru.itmo.hungergames.model.response.ChatResponse;
import ru.itmo.hungergames.model.response.MessageResponse;
import ru.itmo.hungergames.service.ChatService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public List<ChatResponse> getChats() {
        return chatService.getChatsByUserId();
    }

    @GetMapping("/message/{chatId}")
    public List<MessageResponse> getMessagesByChatId(@PathVariable UUID chatId) {
        return chatService.getMessagesByChatId(chatId);
    }

    @MessageMapping("/send/{chatId}")
    @SendTo("/chat/receive/{chatId}")
    public MessageResponse send(@DestinationVariable Long chatId, @Valid MessageRequest messageRequest) {
        return chatService.sendMessage(messageRequest);
    }

    @PostMapping
    @PreAuthorize("hasRole('SPONSOR')")
    public ChatResponse createChat(@RequestBody @Valid ChatCreateRequest chatCreateRequest) {
        return chatService.createChat(chatCreateRequest);
    }
}
