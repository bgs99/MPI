package ru.itmo.hungergames.model.entity;

public enum EventType {
    INTERVIEW,
    MEETING;

    public String humanReadable() {
        switch (this) {
            case INTERVIEW:
                return "Интервью";
            case MEETING:
                return "Встреча";
        }
        throw new RuntimeException("Unexpected event type???");
    }
}
