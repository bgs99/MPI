package ru.itmo.hungergames.exception;

import ru.itmo.hungergames.model.request.ChatCreateRequest;

public class ChatExistsException extends RuntimeException {
    public ChatExistsException(final ChatCreateRequest chatCreateRequest, Long sponsorId) {
        super(String.format("Chat for tribute with id=%s and sponsor with id=%s already exists.",
                chatCreateRequest.getTributeId(), sponsorId));
    }
}
