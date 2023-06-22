package ru.practicum.mainservice;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.StatClient;
import ru.practicum.dto.HitDto;

import java.time.LocalDateTime;

@RestController
public class Controller {
    StatClient statClient = new StatClient("http://localhost:9090", new RestTemplateBuilder());
    @GetMapping("/vui")
    public HitDto test() {
        HitDto hitDto = HitDto.builder()
                .app("sheet-app")
                .uri("/ask-pole")
                .timestamp(LocalDateTime.now())
                .ip("2.12.85.06")
                .build();
        statClient.addHit(hitDto);
        return hitDto;
    }
}
