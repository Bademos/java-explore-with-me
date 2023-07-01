package ru.practicum.mainservice.models.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.category.CategoryDto;
import ru.practicum.mainservice.models.location.Location;
import ru.practicum.mainservice.models.location.LocationDto;
import ru.practicum.mainservice.models.user.UserDto;
import ru.practicum.mainservice.models.user.UserShortDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class EventShortDto {
    Long id;

    String annotation;

    CategoryDto category;

    Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime eventDate;

    UserShortDto initiator;

    Boolean paid;

    Long views;

    String title;
}