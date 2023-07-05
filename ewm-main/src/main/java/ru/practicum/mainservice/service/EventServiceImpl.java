package ru.practicum.mainservice.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.MainConstantShare;
import ru.practicum.mainservice.exceptions.ConflictException;
import ru.practicum.mainservice.exceptions.NotAvailableException;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.event.EventMapper;
import ru.practicum.mainservice.models.event.PreState;
import ru.practicum.mainservice.models.event.State;
import ru.practicum.mainservice.models.event.dto.EventDtoForSearch;
import ru.practicum.mainservice.models.event.dto.NewEventDto;
import ru.practicum.mainservice.models.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.models.user.User;
import ru.practicum.mainservice.repository.CategoryRepository;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {
    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    ClientService clientService;


    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException("There is no event with id:" + id));
    }

    @Override
    public List<Event> getListOfEvents(int from, int size) {
        PageRequest pr = PageRequest.of(from / size, size, MainConstantShare.sortDesc);
        return eventRepository.findAll(pr).stream().collect(Collectors.toList());
    }


    @Override
    public List<Event> getListOfEventsByUser(Long userID, int from, int size) {
        PageRequest pr = PageRequest.of(from / size, size, MainConstantShare.sortDesc);
        return eventRepository.findByInitiatorId(userID, pr);
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event addEvent(NewEventDto event, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("There no user with id:" + userId)
        );
        Category category = categoryRepository.findById(event.getCategory()).orElseThrow(
                () -> new NotFoundException("There no user with id:" + event.getCategory())
        );
        Event eventToSave = EventMapper.makeEventFromDto(event);
        eventToSave.setInitiator(user);
        eventToSave.setCategory(category);
        eventToSave.setConfirmedRequests(0L);
        eventToSave.setViews(0L);

        return eventRepository.save(eventToSave);
    }

    @Override
    public Event updateEvent(Event event, Long id) {
        getEvent(id);
        return eventRepository.save(event);
    }

    @Override
    public Event patchEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = getEvent(eventId);
        if (event.getState().equals(State.PUBLISHED) || event.getState().equals(State.CANCELED)) {
            throw new ConflictException("The event had been published");
        }

        if (checkEvent(event)) {
            updateFields(updateEventAdminRequest, event);
            return eventRepository.save(event);
        }
        return event;
    }

    @Override
    public Event getEventSt(Long eventId, HttpServletRequest httpRequest) {
        Event event =  getEvent(eventId);
        clientService.addView(httpRequest);
        String uri = httpRequest.getRequestURI();
        long hits = clientService.getUniqueHits(uri);
        log.info("uri:{} and hits {} and alt:{}", uri, hits, httpRequest.getRequestURI());
        event.setViews(hits);
        eventRepository.save(event);
        return event;
    }

    @Override
    public Event patchEventByUser(Long eventId, Long userId, UpdateEventAdminRequest upd) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id:" + userId + " is absent.")
        );
        Event event = getEvent(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotAvailableException("Event with id:" + eventId + " cannot  be edited by user with id:" + userId);
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            updateFields(upd, event);
            return eventRepository.save(event);
        } else {
            throw new ConflictException("Forbidden operation");
        }
    }

    @Override
    public List<Event> searchEventsComp(EventDtoForSearch request, HttpServletRequest httpRequest) {
        if (httpRequest != null) {
            clientService.addView(httpRequest);
        }

        timeRangeCheck(request);

        Pageable pageable = PageRequest.of(request.getFrom() / request.getSize(), request.getSize());

        if (request.getQuery() == null && request.getPaid() == null
                && request.getStart() == null && request.getEnd() == null
                && request.getOnlyAvailable() == null && request.getSort() == null
                && request.getCategoryId() == null) {
            return eventRepository.findAll(pageable).toList();
        }

        if (request.getQuery() == null && request.getPaid() == null
                && request.getStart() == null && request.getEnd() == null
                && request.getOnlyAvailable() == null && request.getSort() == null
                && request.getCategoryId() != null)  {
            return eventRepository.findAllByCategoryIdIn(request.getCategoryId(), pageable);
        }

        if (request.getQuery() == null && request.getPaid() == null
                && request.getOnlyAvailable() == null && request.getSort() == null
                && request.getCategoryId() != null)  {
            return eventRepository.findAllByCategoryIdIn(request.getCategoryId(), pageable);
        }

        List<Event> eventList = new ArrayList<>();
        List<Event> sortList = new ArrayList<>();
        if (request.getStart() == null && request.getEnd() == null) {
            eventList = eventRepository.searchAllByAnnotationAndCategoryIdInAndStateIsAndEventDateIsAfter(request.getQuery(),
                    request.getCategoryId(), State.PUBLISHED, LocalDateTime.now(), pageable);
            if (eventList.size() == 0) {
                eventList = eventRepository.searchAllByDescriptionAndCategoryIdInAndStateIsAndEventDateIsAfter(request.getQuery(),
                        request.getCategoryId(), State.PUBLISHED, LocalDateTime.now(), pageable);
            }
        } else {
            eventList = eventRepository
                    .searchAllByAnnotationAndCategoryIdInAndStateIsAndEventDateIsAfterAndEventDateIsBefore(request.getQuery(),
                            request.getCategoryId(), State.PUBLISHED, request.getStart(), request.getEnd(), pageable);
            if (eventList.size() == 0) {
                eventList = eventRepository
                        .searchAllByDescriptionAndCategoryIdInAndStateIsAndEventDateIsAfterAndEventDateIsBefore(request.getQuery(),
                                request.getCategoryId(), State.PUBLISHED, request.getStart(), request.getEnd(), pageable);
            }
        }

        handleRequestPaid(request, eventList, sortList);

        handleRequestNotAvailable(request, eventList, sortList);
        return eventList;
    }

    @Override
    public void deleteEvent(Long id) {
        getEvent(id);
        eventRepository.deleteById(id);
    }

    private boolean checkEvent(Event event) {
        if (event.getState().equals(State.CANCELED)
                || event.getState().equals(State.PENDING)) {
            return event.getEventDate().isAfter(LocalDateTime.now().plusHours(1));
        }
        return false;
    }

    private void updateFields(UpdateEventAdminRequest upd, Event event) {
        Category newCategory = event.getCategory();
        checkUpdatedTime(upd);

        if (upd.getCategory() != null) {
            newCategory = categoryRepository.findById(upd.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category with id" + upd.getCategory() + "is absent"));
        }
        String stateAction = upd.getStateAction();

        if (stateAction != null) {
            if (stateAction.equals(PreState.REJECT_EVENT.toString())) event.setState(State.CANCELED);
            if (stateAction.equals(PreState.PUBLISH_EVENT.toString())) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (stateAction.equals(PreState.CANCEL_REVIEW.toString())) event.setState(State.CANCELED);
            if (stateAction.equals(PreState.SEND_TO_REVIEW.toString())) event.setState(State.PENDING);
        }

        if (!event.getState().equals(State.CANCELED)) {

            if (upd.getAnnotation() != null) event.setAnnotation(upd.getAnnotation());

            if (upd.getCategory() != null) {
                event.setCategory(newCategory);
            }

            if (upd.getDescription() != null) event.setDescription(upd.getDescription());

            if (upd.getTitle() != null) event.setTitle(upd.getTitle());

            if (upd.getEventDate() != null && upd.getEventDate().isAfter(event.getEventDate())) {
                if (upd.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
                    event.setEventDate(event.getEventDate());
                }
            }

            if (upd.getLocation() != null && upd.getLocation().getId() != null) {
                event.setLocation(event.getLocation());
            }

            if (upd.getPaid() != null) event.setPaid(upd.getPaid());

            if (upd.getParticipantLimit() != null) event.setParticipantLimit(upd.getParticipantLimit());

            if (upd.getRequestModeration() != null) event.setRequestModeration(upd.getRequestModeration());

            if (upd.getParticipantLimit() != null) event.setParticipantLimit(event.getParticipantLimit());

            if (upd.getTitle() != null) event.setTitle(event.getTitle());
        }
    }

    private void setState(String stateAction, Event event) {
        if (stateAction != null) {
            if (stateAction.equals(PreState.REJECT_EVENT.toString())) event.setState(State.CANCELED);
            if (stateAction.equals(PreState.PUBLISH_EVENT.toString())) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (stateAction.equals(PreState.CANCEL_REVIEW.toString())) event.setState(State.CANCELED);
            if (stateAction.equals(PreState.SEND_TO_REVIEW.toString())) event.setState(State.PENDING);
        }
    }

    private static void checkUpdatedTime(UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getEventDate() != null) {
            if (LocalDateTime.now().plusHours(MainConstantShare.housrsAfterEvent).isAfter(updateEventAdminRequest.getEventDate())) {
                throw new NotAvailableException("Event time is too late");
            }
        }
    }


    private void handleMainRequest(EventDtoForSearch request, List<Event> eventList, List<Event> sortList, Pageable pageable) {
        if (request.getStart() == null && request.getEnd() == null) {
            eventList = eventRepository.searchAllByAnnotationAndCategoryIdInAndStateIsAndEventDateIsAfter(request.getQuery(),
                    request.getCategoryId(), State.PUBLISHED, LocalDateTime.now(), pageable);
            if (eventList.size() == 0) {
                eventList = eventRepository.searchAllByDescriptionAndCategoryIdInAndStateIsAndEventDateIsAfter(request.getQuery(),
                        request.getCategoryId(), State.PUBLISHED, LocalDateTime.now(), pageable);
            }
        } else {
            eventList = eventRepository
                    .searchAllByAnnotationAndCategoryIdInAndStateIsAndEventDateIsAfterAndEventDateIsBefore(request.getQuery(),
                            request.getCategoryId(), State.PUBLISHED, request.getStart(), request.getEnd(), pageable);
            if (eventList.size() == 0) {
                eventList = eventRepository
                        .searchAllByDescriptionAndCategoryIdInAndStateIsAndEventDateIsAfterAndEventDateIsBefore(request.getQuery(),
                                request.getCategoryId(), State.PUBLISHED, request.getStart(), request.getEnd(), pageable);
            }
        }
    }






    private void handleRequestNotAvailable(EventDtoForSearch request, List<Event> eventList, List<Event> sortList) {
        if (request.getOnlyAvailable() != null) {
            if (request.getOnlyAvailable()) {
                for (Event event : eventList) {
                    if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                        sortList.add(event);
                    }
                }
                eventList.clear();
                eventList.addAll(sortList);
            }
        }
    }

    private void handleRequestPaid(EventDtoForSearch request, List<Event> eventList, List<Event> sortList) {
            if (request.getPaid() != null) {
                if (request.getPaid()) {
                    for (Event event : eventList) {
                        if (event.getPaid()) {
                            sortList.add(event);
                        }
                    }
                    eventList.clear();
                    eventList.addAll(sortList);
                } else {
                    for (Event event : eventList) {
                        if (!event.getPaid()) {
                            sortList.add(event);
                        }
                    }
                    eventList.clear();
                    eventList.addAll(sortList);
                }
            }
        }

        private void timeRangeCheck(EventDtoForSearch request) {
            if (request.getStart() != null && request.getEnd() != null) {
                if (request.getEnd().isBefore(request.getStart())) {
                    throw new NotAvailableException("Incorrect time range");
                }
            }
        }
    }