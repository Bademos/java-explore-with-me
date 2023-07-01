package ru.practicum.mainservice.models.location;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Entity
@Table(name = "location", schema = "public")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "lat", nullable = false)
    Float lat;

    @Column(name = "lon", nullable = false)
    Float lon;
}
