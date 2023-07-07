package ru.practicum.mainservice.controllers.publique;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.event.EventMapper;
import ru.practicum.mainservice.models.event.State;
import ru.practicum.mainservice.models.event.dto.EventDtoForSearch;
import ru.practicum.mainservice.models.event.dto.EventFullDto;
import ru.practicum.mainservice.models.event.dto.EventShortDto;
import ru.practicum.mainservice.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PublicEventController {
    EventService eventService;

    @GetMapping
    public List<EventShortDto> findEvents(@RequestParam(name = "text", required = false) String text,
                                          @RequestParam(name = "categories", required = false) List<Long> categories,
                                          @RequestParam(name = "paid", required = false) Boolean paid,
                                          @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = ConstantsShare.datePattern) LocalDateTime rangeStart,
                                          @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = ConstantsShare.datePattern) LocalDateTime rangeEnd,
                                          @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                          @RequestParam(name = "sort", required = false) String sort,
                                          @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                          HttpServletRequest httpRequest
    ) {

        EventDtoForSearch request = EventDtoForSearch.builder()
                .start(rangeStart)
                .end(rangeEnd)
                .query(text)
                .categoryId(categories)
                .paid(paid)
                .sort(sort)
                .onlyAvailable(onlyAvailable)
                .from(from)
                .size(size)
                .build();
        log.info("Got request for search events in range between {} and {}.Caterories for searching:{}", rangeStart, rangeEnd, categories);
        log.info("There is request:{}", request);

        return eventService.searchEventsComp(request, httpRequest).stream()
                .map(EventMapper::makeShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable @Positive Long id, HttpServletRequest httpRequest) {
        log.info("Got request for event with id:{}", id);
        Event event = eventService.getEventSt(id, httpRequest);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Unfortunately event is not published.");
        }
        return EventMapper.makeFullDto(event);
    }
}