package ru.itmo.hungergames.exception;

public class NoAccessToChatException extends RuntimeException {
    public NoAccessToChatException(Long chatId) {
        super(String.format("You have no access to the chat with id=%s.", chatId));
    }
}
