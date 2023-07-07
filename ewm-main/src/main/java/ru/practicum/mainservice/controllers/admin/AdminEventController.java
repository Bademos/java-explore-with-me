package ru.practicum.mainservice.controllers.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ConstantsShare;
import ru.practicum.mainservice.models.event.dto.EventDtoForSearch;
import ru.practicum.mainservice.models.event.dto.EventFullDto;
import ru.practicum.mainservice.models.event.EventMapper;
import ru.practicum.mainservice.models.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {
    EventService eventService;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable @Positive Long eventId,
                                    @RequestBody @Valid UpdateEventAdminRequest upd) {
        log.info("Got request for patching with title{} and entity {} ", upd.getTitle(), upd);
        return EventMapper.makeFullDto(eventService.patchEvent(eventId, upd));
    }

    @GetMapping
    public List<EventFullDto> getAllForAdmin(@RequestParam(name = "text", required = false) String text,
                                             @RequestParam(name = "categories", required = false) List<Long> categories,
                                             @RequestParam(name = "paid", required = false) Boolean paid,
                                             @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = ConstantsShare.datePattern) LocalDateTime rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = ConstantsShare.datePattern) LocalDateTime rangeEnd,
                                             @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                             @RequestParam(name = "sort", required = false) String sort,
                                             @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") @Positive Integer size
                                             ) {
        log.info("got request for list of events");
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

        return eventService.searchEventsComp(request, null).stream()
                .map(EventMapper::makeFullDto)
                .collect(Collectors.toList());
    }
}