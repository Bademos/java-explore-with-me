package ru.practicum.mainservice.models.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.mainservice.models.location.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventAdminRequest {
    //@NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    //@NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    private LocalDateTime eventDate;

    private Location location;

    private boolean paid;

    private Integer participantLimit;

    private boolean requestModeration;

    private String stateAction;

    //@NotBlank
    @Size(min = 3, max = 120)
    private String title;

    public boolean isPaid() {
        return paid;
    }

    public boolean isRequestModeration() {
        return requestModeration;
    }
}