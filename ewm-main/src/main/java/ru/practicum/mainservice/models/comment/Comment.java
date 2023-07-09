package ru.practicum.mainservice.models.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.user.User;


import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comment", schema = "public")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "text", nullable = false)
    String text;

    @ManyToOne
    @JoinColumn(name = "commentator", nullable = false)
    User commentator;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @Column(name = "comment_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime date;
}
