package ru.itmo.hungergames.model.entity;

public enum UserRole {
    TRIBUTE,
    MENTOR,
    SPONSOR;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
