package ru.practicum.mainservice;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import ru.practicum.dto.ConstantsShare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class MainConstantShare {
    public final Sort SORT_DESC = Sort.by(Sort.Direction.DESC, "id");
    public final Sort SORT_ASC = Sort.by(Sort.Direction.ASC, "id");

    public final Integer HOURS_AFTER_EVENT = 1;
    public final String HTTP_SERVER_TEST = "http://localhost:9090";
    public final String HTTP_SERVER = "http://stats-server:9090";
    public final String START_DATE_STRING = LocalDateTime.of(2022, 02, 24, 5, 0).format(DateTimeFormatter.ofPattern(ConstantsShare.datePattern));


}
