package ru.practicum.mainservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatClient;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.dto.GetStatDto;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.mainservice.exceptions.ServerStatException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;


@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClientService {
    StatClient statClient = new StatClient("http://localhost:9090", new RestTemplateBuilder());
    String appName = "ewm-main";


    public void addView(HttpServletRequest request) {
        statClient.addHit(HitDto.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
        }

    public ResponseEntity<Object> requestStatFromStatService(String uri) {
         GetStatDto viewStatsRequest = GetStatDto.builder()
                 .start(LocalDateTime.of(2022, 02, 24, 5, 0).format(DateTimeFormatter.ofPattern(ConstantsShare.datePattern)))
                 .end(LocalDateTime.now().format(DateTimeFormatter.ofPattern(ConstantsShare.datePattern)))
                 .uris(List.of(uri))
                 .unique(true)
                 .build();

         log.info("stat-server request:{}, end:{}", viewStatsRequest, viewStatsRequest.getEnd());
        return statClient.getStats(viewStatsRequest);
    }

    public List<ViewStatDto> getViewStat(ResponseEntity<Object> responseEntity) {

        if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getBody() == null) {
            throw new ServerStatException("Problem in respopse");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String responseBody = objectMapper.writeValueAsString(responseEntity.getBody());
            return objectMapper.readValue(responseBody, new TypeReference<List<ViewStatDto>>() {
            });
        } catch (JsonProcessingException e) {
            throw new ServerStatException("Problem in json-convert");
        }
    }

    public long getUniqueHits(String uri) {
        List<ViewStatDto> viewStats = getViewStat(requestStatFromStatService(uri));
        return viewStats.stream().findFirst().map(ViewStatDto::getHits).orElse(0L);
    }
}
