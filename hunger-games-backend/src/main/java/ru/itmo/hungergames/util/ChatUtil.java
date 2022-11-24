package ru.itmo.hungergames.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.hungergames.exception.ChatExistsException;
import ru.itmo.hungergames.exception.NoAccessToChatException;
import ru.itmo.hungergames.model.entity.UserRole;
import ru.itmo.hungergames.model.request.ChatCreateRequest;
import ru.itmo.hungergames.repository.ChatRepository;

import java.util.UUID;

@Component
public class ChatUtil {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatUtil(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public void validateBeforeCreate(ChatCreateRequest chatCreateRequest, UUID sponsorId) {
        if (chatRepository.existsAllByTribute_IdAndSponsor_id(
                chatCreateRequest.getTributeId(), sponsorId)) {
            throw new ChatExistsException(chatCreateRequest, sponsorId);
        }
    }

    public void validateBeforeGettingMessagesByChatId(UUID chatId, UUID userId, UserRole userRole) {
        boolean exists = false;
        switch (userRole) {
            case TRIBUTE:
                exists = chatRepository.existsByIdAndTribute_Id(chatId, userId);
                break;
            case SPONSOR:
                exists = chatRepository.existsByIdAndSponsor_Id(chatId, userId);
                break;
            case MENTOR:
                exists = chatRepository.existsByIdAndTribute_Mentor_Id(chatId, userId);
                break;
        }
        if (!exists) {
            throw new NoAccessToChatException(chatId);
        }
    }
}
