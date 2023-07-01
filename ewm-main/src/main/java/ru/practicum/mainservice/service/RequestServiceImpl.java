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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

        log.info("Got request for paticipation request for user with id:{} and event with id:{}" , userId, eventId);

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event does not found")
        );

        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException("Request is already exist");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Requestor cannot be initiator");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("The event is published");
        }


        //if (event.getParticipantLimit() >=0 && event.getParticipantLimit() <= event.getConfirmedRequests().intValue()) {
        //    throw new ConflictException( "There is request limit is over");
        //}

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests())
            throw new ConflictException("достигнут лимит запросов на участие");



        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User does not found")
        );
        log.info("User with id {} found , his name is ", userId, user.getName());



        if (!userId.equals(event.getInitiator().getId())) {
            Request request = Request.builder()
                    .requester(user)
                    .status(State.PENDING)
                    //.status((event.getParticipantLimit() == 0) || (!event.getRequestModeration())
                    //        ? State.CONFIRMED
                    //        : State.PENDING
                    //)
                    .event(event)
                    .created(LocalDateTime.now())
                    .build();
            request =  requestRepository.save(request);
            /*if (request.getStatus().equals(State.CONFIRMED)) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
            }*/
            //event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            //eventRepository.save(event);
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
        /*if(event.getState().equals(State.CONFIRMED)) {
            throw new ConflictException("");
        }*/

        if (event.getParticipantLimit() == null) event.setParticipantLimit(0);
        if (event.getConfirmedRequests() == null) event.setConfirmedRequests(0L);;
        if (event.getParticipantLimit() >=0 && event.getParticipantLimit() <= event.getConfirmedRequests().intValue()) {
            throw new ConflictException( "There is request limit is over");
        }


        List<Request> eventRequestList = requestRepository.findByIdIn((updateRequest.getRequestIds()));
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();
        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult();

        if (event.getParticipantLimit() == null) event.setParticipantLimit(0);
        if (event.getConfirmedRequests() == null) event.setConfirmedRequests(0L);



        if (updateRequest.getStatus().equals(State.REJECTED)) {
            for (Request eventRequest : eventRequestList) {
                eventRequest.setStatus(State.REJECTED);
                rejectedRequests.add(eventRequest);
            }
        }

        if (event.getParticipantLimit() == 0) {
            confirmedRequests.addAll(eventRequestList);
            updateResult.setConfirmedRequests(confirmedRequests.stream().map(RequestMapper::makeRequestDto).collect(Collectors.toList()));
            return updateResult;
        }
        for (Request eventRequest : eventRequestList) {


            if (event.getParticipantLimit() >=0 && event.getParticipantLimit() <= event.getConfirmedRequests().intValue()) {
                throw new ConflictException( "There is request limit is over");
            }

            if (event.getConfirmedRequests().intValue() <= event.getParticipantLimit()) {
                if (eventRequest.getStatus().equals(State.PENDING)) {
                    eventRequest.setStatus(State.CONFIRMED);
                    confirmedRequests.add(eventRequest);
                    //event.setParticipantLimit(event.getParticipantLimit() + 1);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);

                }
            } else {
                eventRequest.setStatus(State.REJECTED);
                rejectedRequests.add(eventRequest);
            }
        }
        updateResult.setConfirmedRequests(confirmedRequests.stream().map(RequestMapper::makeRequestDto).collect(Collectors.toList()));
        updateResult.setRejectedRequests(rejectedRequests.stream().map(RequestMapper::makeRequestDto).collect(Collectors.toList()));
        eventRepository.save(event);
        return updateResult;
    }


    private Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("There is no request with id:" + requestId + " in db")
        );
    }
}