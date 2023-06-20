package ru.practicum.server.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatServerController {
    StatServiceImplementation statService;

    public StatServerController(StatServiceImplementation hitService) {
        this.statService = hitService;
    }

    @PostMapping(ConstantsShare.hitAddr)
    public String addHit(@RequestBody @Valid HitDto hitDto) {
        Hit hit = HitMapper.makeHitFromHitDto(hitDto);
        log.info("Get request to add endpoint hit: {} in DB", hit);
        statService.addHit(hit);
        return "Ok";
    }

    @GetMapping(ConstantsShare.statAddr)
    public List<ViewStatDto> getStat(@RequestParam @NotNull String start,
                                     @RequestParam @NotNull String end,
                                     @RequestParam(required = false) List<String> uris,
                                     @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Get request to get statistics.");

        return statService.getStat(LocalDateTime.parse(start, DateTimeFormatter.ofPattern(ConstantsShare.datePattern)),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern(ConstantsShare.datePattern)),
                uris, unique);
    }
}