package ru.itmo.hungergames.exception;

public class NotCorrectDateException extends RuntimeException {
    public NotCorrectDateException(String message) {
        super(message);
    }
}
