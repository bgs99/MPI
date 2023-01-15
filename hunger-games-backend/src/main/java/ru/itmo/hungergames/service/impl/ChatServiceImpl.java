package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.chat.Chat;
import ru.itmo.hungergames.model.entity.chat.Message;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.request.ChatCreateRequest;
import ru.itmo.hungergames.model.request.MessageRequest;
import ru.itmo.hungergames.model.response.ChatResponse;
import ru.itmo.hungergames.model.response.MessageResponse;
import ru.itmo.hungergames.repository.*;
import ru.itmo.hungergames.service.ChatService;
import ru.itmo.hungergames.util.ChatUtil;
import ru.itmo.hungergames.util.SecurityUtil;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final TributeRepository tributeRepository;
    private final SponsorRepository sponsorRepository;
    private final SecurityUtil securityUtil;
    private final ChatUtil chatUtil;
    private final UserRepository userRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository,
                           MessageRepository messageRepository,
                           TributeRepository tributeRepository,
                           SponsorRepository sponsorRepository,
                           SecurityUtil securityUtil,
                           ChatUtil chatUtil,
                           UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.tributeRepository = tributeRepository;
        this.sponsorRepository = sponsorRepository;
        this.securityUtil = securityUtil;
        this.chatUtil = chatUtil;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ChatResponse createChat(ChatCreateRequest chatCreateRequest) {
        chatUtil.validateBeforeCreate(chatCreateRequest, securityUtil.getAuthenticatedUserId());
        Sponsor sponsor = sponsorRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with the ID"));
        Tribute tribute = tributeRepository
                .findById(chatCreateRequest.getTributeId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with the ID"));

        Chat chat = chatRepository.save(Chat.builder()
                .tribute(tribute)
                .sponsor(sponsor)
                .build());
        return new ChatResponse(chat);
    }

    @Override
    @Transactional
    public MessageResponse sendMessage(UUID chatId, MessageRequest messageRequest) {
        User user = securityUtil.getAuthenticatedUser();
        Chat chat = chatRepository
                .findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("There's no chat with the ID"));

        return new MessageResponse(messageRepository.save(
                Message.builder()
                    .message(messageRequest.getMessage())
                    .user(user)
                    .chat(chat)
                    .dateTime(Instant.now())
                    .build()));
    }

    @Override
    public List<MessageResponse> getMessagesByChatId(UUID chatId) {
        chatUtil.validateBeforeGettingMessagesByChatId(chatId,
                securityUtil.getAuthenticatedUserId(), securityUtil.getAuthenticatedUserRole());
        return messageRepository.findAllByChat_Id(chatId).stream()
                .map(MessageResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatResponse> getChatsByUserId() {
        List<Chat> chats;
        UUID userId = securityUtil.getAuthenticatedUserId();
        chats = switch (securityUtil.getAuthenticatedUserRole()) {
            case MENTOR -> chatRepository.findAllByTribute_Mentor_Id(userId);
            case SPONSOR -> chatRepository.findAllBySponsor_Id(userId);
            case TRIBUTE -> chatRepository.findAllByTribute_Id(userId);
            default -> Collections.emptyList();
        };
        return chats.stream()
                .map(ChatResponse::new)
                .collect(Collectors.toList());
    }
}
