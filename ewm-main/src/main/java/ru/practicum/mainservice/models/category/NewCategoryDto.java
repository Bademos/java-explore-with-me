package ru.practicum.mainservice.models.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class NewCategoryDto {
    @NotBlank
    @Size(max=50)
    String name;
}
