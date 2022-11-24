package ru.itmo.hungergames.exception;

import java.util.UUID;

public class NoAccessToChatException extends RuntimeException {
    public NoAccessToChatException(UUID chatId) {
        super(String.format("You have no access to the chat with id=%s.", chatId));
    }
}
