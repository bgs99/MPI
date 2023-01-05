package ru.itmo.hungergames.model.entity.user;

public enum UserRole {
    TRIBUTE(0),
    MENTOR(1),
    SPONSOR(2),
    MODERATOR(3);

    private final int value;
    UserRole(int value) {
        this.value = value;
    }

    public int asInt() {
        return value;
    }

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
