package ru.itmo.hungergames.model.entity.user;

public enum UserRole {
    TRIBUTE,
    MENTOR,
    SPONSOR,
    MODERATOR;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
