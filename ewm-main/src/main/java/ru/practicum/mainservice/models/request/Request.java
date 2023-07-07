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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "requests", schema = "public")

public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "event")
    Event event;

    @ManyToOne
    @JoinColumn(name = "requester", nullable = false)
    User requester;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state", nullable = false)
    State status;

    @Column(name = "created", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime created;
}
