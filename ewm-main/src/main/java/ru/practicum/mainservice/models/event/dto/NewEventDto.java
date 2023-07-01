package ru.practicum.mainservice.models.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.mainservice.models.location.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class NewEventDto {
    @NotBlank
    @Size(max = 2000, min = 20)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(max = 7000, min = 20)
    private String description;

    @NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @NotNull
    @Size(min = 3, max = 120)
    private String title;

    private boolean paid;

    @PositiveOrZero
    private int participantLimit = 0;

    private boolean requestModeration = true;
}
