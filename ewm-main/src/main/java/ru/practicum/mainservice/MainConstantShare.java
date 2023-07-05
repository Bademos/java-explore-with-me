package ru.practicum.mainservice;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import ru.practicum.dto.ConstantsShare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class MainConstantShare {
    public final Sort sortDesc = Sort.by(Sort.Direction.DESC, "id");
    public final Sort sortAsc = Sort.by(Sort.Direction.ASC, "id");

    public final Integer housrsAfterEvent = 1;
    public final String httpServerTest = "http://localhost:9090";
    public final String httpServer = "http://stats-server:9090";
    public final String startDateString = LocalDateTime.of(2022, 02, 24, 5, 0).format(DateTimeFormatter.ofPattern(ConstantsShare.datePattern));


}
