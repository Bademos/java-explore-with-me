package ru.practicum.mainservice.models.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDtoUpd {
    Boolean pinned = false;

    @Size(max = 50)
    String title;

    List<Long> events;
}
