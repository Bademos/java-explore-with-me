package ru.practicum.mainservice.models.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.mainservice.models.event.dto.EventShortDto;
import ru.practicum.mainservice.models.user.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;

    @NotBlank
    @Size(max = 20000)
    String text;

    @NotNull
    UserDto commentator;

    @NotNull
    EventShortDto event;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime date;
}
