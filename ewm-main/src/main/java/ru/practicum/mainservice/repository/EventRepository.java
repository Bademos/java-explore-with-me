package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.event.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInitiatorId(Long userId, Pageable pageRequest);
    Set<Event> findAllByIdIn(List<Long> idList);

    List<Event> findAllByCategoryId(Long categoryId);


    Event findByIdAndInitiatorId(Long id, Long eventId);

    Event findByIdAndStateIs(Long eventId, State state);

    List<Event> findEventsByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(
            List<Long> initiatorIds, State[] states, List<Long> categoryIds,
            LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Event> searchAllByAnnotationAndCategoryIdInAndStateIsAndEventDateIsAfterAndEventDateIsBefore(
            String query, List<Long> categoryId, State state, LocalDateTime start, LocalDateTime end, Pageable pageable);
    List<Event> searchAllByDescriptionAndCategoryIdInAndStateIsAndEventDateIsAfterAndEventDateIsBefore(
            String query, List<Long> categoryId, State state, LocalDateTime start, LocalDateTime end, Pageable pageable);
    List<Event> searchAllByAnnotationAndCategoryIdInAndStateIsAndEventDateIsAfter(
            String query, List<Long> categoryId, State state, LocalDateTime start,  Pageable pageable);
    List<Event> searchAllByDescriptionAndCategoryIdInAndStateIsAndEventDateIsAfter(
            String query, List<Long> categoryId, State state, LocalDateTime start, Pageable pageable);
}
