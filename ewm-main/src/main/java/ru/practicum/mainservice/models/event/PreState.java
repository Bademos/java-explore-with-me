package ru.practicum.mainservice.models.event;

public enum PreState {
    SEND_TO_REVIEW(State.PENDING),
    CANCEL_REVIEW(State.CANCELED),
    PUBLISH_EVENT(State.PUBLISHED),
    REJECT_EVENT(State.CANCELED);

    private final State state;

    PreState(State state) {
        this.state = state;
    }
}
