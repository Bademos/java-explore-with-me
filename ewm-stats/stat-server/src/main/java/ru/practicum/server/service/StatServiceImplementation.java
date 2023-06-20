package ru.practicum.server.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.server.model.Hit;
import ru.practicum.server.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatServiceImplementation implements StatService {
    HitRepository hitRepository;

    public StatServiceImplementation(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    @Override
    public Hit addHit(Hit hit) {
        log.info("adding endpoint hit {} in db", hit);
        return hitRepository.save(hit);
    }

    @Override
    public List<ViewStatDto> getStat(LocalDateTime start, LocalDateTime end,
                                     List<String> uris, boolean unique) {
        if (unique) {
            log.info("Get request for statistics of unique uri in list {}", uris);
            return hitRepository.getUniqueHits(start, end, uris);
        }
        log.info("Get request for statistics of all uri in list {}", uris);
        return hitRepository.getHits(start, end, uris);
    }
}
