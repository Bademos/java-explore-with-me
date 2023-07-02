package ru.practicum.mainservice.service;

import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.event.dto.NewEventDto;
import ru.practicum.mainservice.models.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    Event getEvent(Long id);

    List<Event> getListOfEvents(int from, int size);

    List<Event> getListOfEventsByUser(Long userId, int from, int size);

    Event addEvent(Event event);

    Event addEvent(NewEventDto event, Long userId);

    Event updateEvent(Event event, Long id);

    Event patchEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    Event getEventSt(Long eventId);

    Event patchEventByUser(Long eventId, Long userId, UpdateEventAdminRequest updateEventAdminRequest);

    List<Event> searchEvents(String query, List<Long> categoryIds, Boolean paid, LocalDateTime start,
                             LocalDateTime end, Boolean onlyAvailable, String sort, Integer from,
                             Integer size);

    void deleteEvent(Long id);
}
