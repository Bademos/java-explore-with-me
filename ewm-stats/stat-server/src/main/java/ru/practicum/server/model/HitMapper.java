package ru.practicum.server.model;

import ru.practicum.dto.HitDto;

public class HitMapper {
    public static Hit makeHitFromHitDto(HitDto hitDto) {
        return Hit.builder().app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getUri())
                .timestamp(hitDto.getTimestamp())
                .build();
    }
}
