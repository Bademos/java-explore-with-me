package ru.practicum.mainservice.models.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.models.event.State;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    State status;
}
