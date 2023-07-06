package ru.practicum.mainservice.models.event;

import lombok.Getter;
import ru.practicum.mainservice.exceptions.NotAvailableException;

@Getter
public enum PreState {
    SEND_TO_REVIEW(State.PENDING),
    CANCEL_REVIEW(State.CANCELED),
    PUBLISH_EVENT(State.PUBLISHED),
    REJECT_EVENT(State.CANCELED);

    private final State state;

    PreState(State state) {
        this.state = state;
    }

    public static PreState from(String name) {
        for (PreState preState : values()) {
            if (preState.name().equalsIgnoreCase(name)) {
                return preState;
            }
        }
        throw new NotAvailableException("Unknown event state action: " + name);
    }
}
