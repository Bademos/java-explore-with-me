package ru.practicum.mainservice.models.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.models.location.Location;
import ru.practicum.mainservice.models.location.LocationDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class EventDto {
    Long id;

    String description;

    String state;

    LocationDto location;

}
