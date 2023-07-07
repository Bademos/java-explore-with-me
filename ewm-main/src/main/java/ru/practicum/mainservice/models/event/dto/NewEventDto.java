package ru.practicum.mainservice.models.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.models.location.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Valid
public class NewEventDto {
    @NotBlank
    @Size(max = 2000, min = 20)
    String annotation;

    @NotNull
    Long category;

    @NotBlank
    @Size(max = 7000, min = 20)
    String description;

    @NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;


    @NotNull
    LocationDto location;

    @NotNull
    @Size(min = 3, max = 120)
    String title;

    boolean paid;

    @PositiveOrZero
    int participantLimit;

    boolean requestModeration = true;
}
