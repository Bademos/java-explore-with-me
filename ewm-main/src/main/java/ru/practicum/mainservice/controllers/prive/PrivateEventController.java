package ru.practicum.mainservice.controllers.prive;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.exceptions.ConflictException;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.event.dto.*;
import ru.practicum.mainservice.models.event.EventMapper;
import ru.practicum.mainservice.models.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.models.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.models.request.RequestDto;
import ru.practicum.mainservice.models.request.RequestMapper;
import ru.practicum.mainservice.service.EventService;
import ru.practicum.mainservice.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    EventService eventService;
    RequestService requestService;

    @GetMapping
    public List<EventShortDto> getAllByUser(@PathVariable @Positive Long userId,
                                            @RequestParam(name = "from", defaultValue = "0") int from,
                                            @RequestParam(name = "size", defaultValue = "10") int size) {
        return eventService.getListOfEventsByUser(userId, from, size).stream()
                .map(EventMapper::makeShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventFullDto getEventByIDForUser(@PathVariable @Positive Long userId,
                                            @PathVariable @Positive Long id) {
        log.info("Got request for event with id:{} by user:{}", id, userId);
        Event event = eventService.getEvent(id);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            return EventMapper.makeFullDto(event);
        } else {
            throw new ConflictException("User has not initiator the event");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable @Positive Long userId,
                                 @RequestBody @Valid NewEventDto event) {
        log.info("Got request to create event:{}, by user with id:{}", event, userId);
        return EventMapper.makeFullDto(eventService.addEvent(event, userId));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable @Positive Long userId,
                                          @PathVariable @Positive Long eventId,
                                          @RequestBody @Valid UpdateEventAdminRequest upd) {

        log.info("Got request to patch event with id:{}, by user with id:{} and new category is {}", eventId, userId, upd.getCategory());

        return EventMapper.makeFullDto(eventService.patchEventByUser(eventId, userId, upd));
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequests(@PathVariable @Positive Long userId,
                                        @PathVariable @Positive Long eventId) {
        log.info("Got request for list of request of participation in event with id:{}, created by user with id:{}", eventId, userId);

        return requestService.getAllByEventAndUser(eventId, userId).stream()
                .map(RequestMapper::makeRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long eventId, @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Got request for list of request of participation in event with id:{}, created by user with id:{}", eventId, userId);
        return requestService.patchRequestsByUserIdAndEventId(userId, eventId, request);
    }
}