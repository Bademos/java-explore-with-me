package ru.practicum.mainservice;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class MainConstantShare {
    public final Sort sortDesc = Sort.by(Sort.Direction.DESC, "id");
    public final Sort sortAsc = Sort.by(Sort.Direction.ASC, "id");

    public final Integer housrsAfterEvent = 1;
    public final String httpServerTest = "http://localhost:9090";
    public final String httpServer = "http://stats-server:9090";
}
