package ru.practicum.client;

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

import java.util.Map;

@Service
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
        if (getStatDto.getUnique() != null) {
            Map<String, Object> params = Map.of(
                    "start", getStatDto.getStart(),
                    "end", getStatDto.getEnd(),
                    "uris", getStatDto.getUris(),
                    "unique", getStatDto.getUnique()
            );
            return get(ConstantsShare.statAddr + "?start={start}&end={end}&uris={uris}&unique={unique}", params);
        }
        Map<String, Object> params = Map.of(
                "start", getStatDto.getStart(),
                "end", getStatDto.getEnd(),
                "uris", getStatDto.getUris()
        );
        return get(ConstantsShare.statAddr + "?start={start}&end={end}&uris={uris}", params);
    }
}
