package ru.practicum.server.service;

import ru.practicum.dto.ViewStatDto;
import ru.practicum.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    Hit addHit(Hit hit);

    List<ViewStatDto> getStat(LocalDateTime start, LocalDateTime end,
                              List<String> uris, boolean unique);
}
