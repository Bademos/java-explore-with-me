package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GetStatDto {
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    //@DateTimeFormat(pattern = ConstantsShare.datePattern)
    private String start;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ConstantsShare.datePattern)
    //@DateTimeFormat(pattern = ConstantsShare.datePattern)
    private String end;

    private List<String> uris;

    private Boolean unique;
}
