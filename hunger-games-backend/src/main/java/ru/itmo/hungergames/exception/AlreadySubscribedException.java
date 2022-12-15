package ru.itmo.hungergames.exception;

public class AlreadySubscribedException extends RuntimeException {
    public AlreadySubscribedException() {
        super("You can't subscribe. You you already have it or payment is not been confirmed yet");
    }
}
