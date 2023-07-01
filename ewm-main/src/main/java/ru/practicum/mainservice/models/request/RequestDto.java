package ru.practicum.mainservice.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.event.State;
import ru.practicum.mainservice.models.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {
    Long id;

    Long event;

    Long requester;

    String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime created;
}
