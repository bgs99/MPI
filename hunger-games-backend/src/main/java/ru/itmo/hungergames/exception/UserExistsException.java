package ru.itmo.hungergames.exception;

import ru.itmo.hungergames.model.entity.User;

public class UserExistsException extends RuntimeException {
    public UserExistsException(final User user) {
        super(String.format("User %s exists.", user.getUsername()));
    }
}
