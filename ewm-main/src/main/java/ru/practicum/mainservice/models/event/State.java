package ru.practicum.mainservice.models.event;

import ru.practicum.mainservice.exceptions.NotAvailableException;

public enum State {
    PENDING,
    PUBLISHED,
    CANCELED,
    CONFIRMED,
    REJECTED,
    UNKNOWN;

    public static State from(String name) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(name)) {
                return state;
            }
        }
        throw new NotAvailableException("There is no posibility to cast the staff");
    }
}