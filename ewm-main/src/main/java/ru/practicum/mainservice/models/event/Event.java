package ru.practicum.mainservice.models.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.location.Location;
import ru.practicum.mainservice.models.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Entity
@Table(name="events",  schema="public")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "annotation", nullable = false)
    String annotation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(name = "confirmed_request")
    Long confirmedRequests;

    @Column(name = "created_on", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime createdOn;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "event_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    User initiator;

    @Column(name = "paid")
    Boolean paid;

    @Column(name = "participant_limit")
    Integer participantLimit;

    @Column(name = "published_on", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    Boolean requestModeration;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    State state;

    @Column (name = "views")
    Long views;

    @Column (name = "title")
    String title;
}
