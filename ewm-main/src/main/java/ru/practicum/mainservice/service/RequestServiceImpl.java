package ru.practicum.mainservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exceptions.ConflictException;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.event.State;
import ru.practicum.mainservice.models.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.models.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.models.request.Request;
import ru.practicum.mainservice.models.request.RequestMapper;
import ru.practicum.mainservice.models.user.User;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.RequestRepository;
import ru.practicum.mainservice.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RequestServiceImpl implements RequestService {
    RequestRepository requestRepository;
    UserRepository userRepository;
    EventRepository eventRepository;

    @Override
    public List<Request> getAllByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User does not found")
        );
        return requestRepository.findAllByRequesterId(userId);
    }

    @Override
    public List<Request> getAllByEventAndUser(Long eventId, Long userId) {
        return requestRepository.findAllByRequesterIdAndEventId(userId + 1, eventId);
    }

    @Override
    public Request cancelRequest(Long requestId, Long userId) {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("There is no request with id:{}" + requestId)
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User does not found")
        );

        if (userId.equals(request.getRequester().getId())) {
            request.setStatus(State.CANCELED);
            return requestRepository.save(request);
        } else {
            throw new NotFoundException("The event was not initiated by user with id:" + userId);
        }
    }

    @Override
    public Request addRequest(Long eventId, Long userId) {

        log.info("Got request for paticipation request for user with id:{} and event with id:{}", userId, eventId);

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event does not found")
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User does not found")
        );
        requestExistCheck(eventId, userId);
        requestorCheck(event, userId);
        isPublishedCheck(event);
        limitParticipiantsCheck(event);

        log.info("User with id {} found , his name is ", userId, user.getName());

        if (!userId.equals(event.getInitiator().getId())) {
            Request request = Request.builder()
                    .requester(user)
                    .status(State.PENDING)
                    .event(event)
                    .created(LocalDateTime.now())
                    .build();

            State requestStatus = (!event.getRequestModeration() || event.getParticipantLimit() == 0)
                    ? State.CONFIRMED : State.PENDING;
            request.setStatus(requestStatus);

            if (requestStatus.equals(State.CONFIRMED)) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            }
            request = requestRepository.save(request);
            return request;
        } else {
            throw new NotFoundException("The event was not initiated by user with id:" + userId);
        }
    }

    @Override
    public EventRequestStatusUpdateResult patchRequestsByUserIdAndEventId(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        List<Request> requestsEvent = requestRepository.findAllByEventId(eventId);

        if (event.getParticipantLimit() <= requestRepository.countByEventIdAndStatus(eventId, State.CONFIRMED)) {
            throw new ConflictException("Limit of paticipiants is over");
        }

        EventRequestStatusUpdateResult eventRequestStatusUpdateResultDto = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();

        List<Request> requestListUpdateStatus = new ArrayList<>();
        for (Request request : requestsEvent) {
            if (updateRequest.getRequestIds().contains(request.getId())) {
                request.setStatus(updateRequest.getStatus());
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                requestListUpdateStatus.add(request);
            }
        }

        for (Request request : requestListUpdateStatus) {
            if (!request.getStatus().equals(State.CANCELED)) {
                eventResultConstructor(eventRequestStatusUpdateResultDto, request);
            } else {
                throw new ConflictException("Error");
            }
        }
        requestRepository.saveAll(requestListUpdateStatus);
        eventRepository.save(event);
        return eventRequestStatusUpdateResultDto;
    }

    private void eventResultConstructor(EventRequestStatusUpdateResult eventRequestStatusUpdateResult, Request request) {
        if (request.getStatus().equals(State.CONFIRMED)) {
            eventRequestStatusUpdateResult.getConfirmedRequests().add(RequestMapper.makeRequestDto(request));
        } else if (request.getStatus().equals(State.REJECTED)) {
            eventRequestStatusUpdateResult.getRejectedRequests().add(RequestMapper.makeRequestDto(request));
        }
    }

    private Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("There is no request with id:" + requestId + " in db")
        );
    }


    private void requestExistCheck(Long eventId, Long userId) {
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException("Request is already exist");
        }
    }

    private void requestorCheck(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Requestor cannot be initiator");
        }
    }

    private void isPublishedCheck(Event event) {
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("The event is published");
        }
    }

    private void limitParticipiantsCheck(Event event) {
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests())
            throw new ConflictException("Limit of paticipiants is over");
    }
}