package ru.practicum.mainservice.models.location;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDto {
    //Long id;

    Float lon;

    Float lat;
}
