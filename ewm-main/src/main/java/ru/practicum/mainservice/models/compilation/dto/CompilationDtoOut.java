package ru.practicum.mainservice.models.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.models.event.dto.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDtoOut {
    Long id;

    Boolean pinned;

    String title;

    List<EventShortDto> events;
}
