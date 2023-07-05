package ru.practicum.mainservice.controllers.prive;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.models.request.RequestDto;
import ru.practicum.mainservice.models.request.RequestMapper;
import ru.practicum.mainservice.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PrivateRequestController {
    RequestService requestService;

    @GetMapping
    public List<RequestDto> getRequestsByUser(@PathVariable @Positive Long userId) {
        log.info("Got reguest for all participation requests of user with id:{}", userId);
        return requestService.getAllByUser(userId).stream()
                .map(RequestMapper::makeRequestDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable @Positive Long userId,
                                 @RequestParam(name = "eventId") Long eventId) {
        log.info("Got request for paticipation request for user with id:{} and event with id:{}", userId, eventId);
        return RequestMapper.makeRequestDto(requestService.addRequest(eventId, userId));
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long requestId) {
        log.info("Got request for updating request for user with id:{} and event with id:{}", userId, requestId);
        return RequestMapper.makeRequestDto(requestService.cancelRequest(requestId, userId));
    }
}