package ru.itmo.hungergames.exception;

public class NotNewsSubscribedException extends RuntimeException {
    public NotNewsSubscribedException() {
        super("You have no access to news. Buy subscription");
    }
}
