package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.dto.GetStatDto;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatDto;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatClient extends BaseClient {
    @Autowired
    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addHit(HitDto hitDto) {
        return post(ConstantsShare.hitAddr, hitDto);
    }

    public ResponseEntity<Object> getStats(GetStatDto getStatDto) {
        log.info("try to send request:{} and try to extract date", getStatDto, getStatDto.getStart());
        if (getStatDto.getUnique() != null) {
            Map<String, Object> params = Map.of(
                    "start", getStatDto.getStart(),
                    "end", getStatDto.getEnd(),
                    "uris", String.join(",", getStatDto.getUris()),
                    "unique", getStatDto.getUnique()
            );
            return get(ConstantsShare.statAddr + "?start={start}&end={end}&uris={uris}&unique={unique}", params);
        }
        Map<String, Object> params = Map.of(
                "start", getStatDto.getStart(),
                "end", getStatDto,
                "uris", String.join(",", getStatDto.getUris())
        );
        return get(ConstantsShare.statAddr + "?start={start}&end={end}&uris={uris}", params);
    }

    public List<ViewStatDto> getStatistic(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", String.join(",", uris),
                "unique", unique
        );
        ResponseEntity<Object> response = get(
                "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                parameters
        );
        ObjectMapper objectMapper = new ObjectMapper();
        List<ViewStatDto> viewStatDto = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
        });
        return viewStatDto;
    }
}
