package ru.practicum.server.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.server.model.Hit;
import ru.practicum.server.model.HitMapper;
import ru.practicum.server.service.StatServiceImplementation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatServerController {
    StatServiceImplementation statService;

    @PostMapping(ConstantsShare.hitAddr)
    public void addHit(@RequestBody @Valid HitDto hitDto) {
        Hit hit = HitMapper.makeHitFromHitDto(hitDto);
        log.info("Get request to add endpoint hit: {} in DB", hit);
        statService.addHit(hit);
    }

    @GetMapping(ConstantsShare.statAddr)
    public List<ViewStatDto> getStat(
            @RequestParam @NotNull @DateTimeFormat(pattern = ConstantsShare.datePattern) LocalDateTime start,
            @RequestParam @NotNull @DateTimeFormat(pattern = ConstantsShare.datePattern) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Get request to get statistics.");

        return statService.getStat(start, end, uris, unique);
    }
}