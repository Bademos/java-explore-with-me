package ru.practicum.mainservice.models.event.dto;

import lombok.*;
import ru.practicum.mainservice.models.location.Location;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {
    private String annotation;

    private Long category;

    private String description;

    private String eventDate;

    private Location location;

    private boolean paid;

    private Integer participantLimit;

    private boolean requestModeration;

    private String stateAction;

    private String title;

    public boolean isPaid() {
        return paid;
    }

    public boolean isRequestModeration() {
        return requestModeration;
    }
}
