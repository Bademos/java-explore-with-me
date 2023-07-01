package ru.practicum.mainservice.models.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.category.CategoryDto;
import ru.practicum.mainservice.models.event.State;
import ru.practicum.mainservice.models.location.Location;
import ru.practicum.mainservice.models.location.LocationDto;
import ru.practicum.mainservice.models.user.User;
import ru.practicum.mainservice.models.user.UserShortDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)

public class EventFullDto {
    Long id;

    @Size(max = 1000)
    String annotation;

    CategoryDto category;

    Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime createdOn;

    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime eventDate;

    LocationDto location;
    UserShortDto initiator;

    Boolean paid;

    @PositiveOrZero
    Integer participantLimit ;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime publishedOn;

    Boolean requestModeration;

    String state;

    Long views;

    String title;
}