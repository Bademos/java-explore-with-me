package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Integer> {
    @Query("SELECT new ru.practicum.dto.ViewStatDto(hit.app, hit.uri, count(hit.ip)) " +
            "FROM Hit as hit " +
            "WHERE hit.timestamp between :start and :end and (:#{#uris == null} = true or hit.uri in :uris)" +
            "GROUP BY hit.app, hit.uri " +
            "order by count(hit.ip) DESC")
    List<ViewStatDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uris);


    @Query("SELECT new ru.practicum.dto.ViewStatDto(hit.app, hit.uri, count(distinct hit.ip)) " +
            "FROM Hit as hit " +
            "WHERE hit.timestamp between :start and :end and (:#{#uris == null} = true or hit.uri in :uris)" +
            "GROUP BY hit.app, hit.uri " +
            "order by count(hit.ip) DESC")
    List<ViewStatDto> getUniqueHits(LocalDateTime start, LocalDateTime end, List<String> uris);
}
