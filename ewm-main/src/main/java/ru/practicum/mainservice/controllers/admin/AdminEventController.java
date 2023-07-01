package ru.practicum.mainservice.controllers.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.event.dto.EventFullDto;
import ru.practicum.mainservice.models.event.dto.EventShortDto;
import ru.practicum.mainservice.models.event.EventMapper;
import ru.practicum.mainservice.models.event.State;
import ru.practicum.mainservice.models.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.models.location.Location;
import ru.practicum.mainservice.models.user.User;
import ru.practicum.mainservice.repository.CategoryRepository;
import ru.practicum.mainservice.repository.LocationRepository;
import ru.practicum.mainservice.repository.UserRepository;
import ru.practicum.mainservice.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
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
    public List<EventFullDto> getAll(@RequestParam(name = "from", defaultValue = "0") int from,
                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("got request for list of events");
        log.info ("upsettable result:{}", eventService.getListOfEvents(from, size));
       // log.info("hoooooooli:{}", eventService.getListOfEvents(from, size).get(0).getLocation());
        return eventService.getListOfEvents(from, size).stream()
                .map(EventMapper::makeFullDto)
                .collect(Collectors.toList());
    }
}
