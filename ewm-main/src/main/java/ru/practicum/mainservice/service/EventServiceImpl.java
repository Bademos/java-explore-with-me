package ru.practicum.mainservice.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.MainConstantShare;
import ru.practicum.mainservice.exceptions.ConflictException;
import ru.practicum.mainservice.exceptions.NotAvailableException;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.event.EventMapper;
import ru.practicum.mainservice.models.event.State;
import ru.practicum.mainservice.models.event.dto.NewEventDto;
import ru.practicum.mainservice.models.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.models.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.models.location.LocationDto;
import ru.practicum.mainservice.models.user.User;
import ru.practicum.mainservice.models.user.UserMapper;
import ru.practicum.mainservice.repository.CategoryRepository;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.LocationRepository;
import ru.practicum.mainservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {
    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException("There is no event with id:" + id));
    }

    @Override
    public List<Event> getListOfEvents(int from, int size) {
        from /= size;
        PageRequest pr = PageRequest.of(from, size, MainConstantShare.sortDesc);

        return eventRepository.findAll(pr).stream().collect(Collectors.toList());
    }


    @Override
    public List<Event> getListOfEventsByUser(Long userID, int from, int size) {
        from /= size;
        PageRequest pr = PageRequest.of(from, size, MainConstantShare.sortDesc);

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
       if (event.getState().equals(State.PUBLISHED) || event.getState().equals(State.REJECTED)) {
            throw new ConflictException("The event had been published");
        }

        /*Category category = categoryRepository.findById(updateEventAdminRequest.getCategory()).orElseThrow(
                () -> new NotFoundException("Category with id" + updateEventAdminRequest.getCategory() + "is absent"));
        */
        boolean check = checkEvent(event);


        if (checkEvent(event)) {
            updateFields(updateEventAdminRequest, event);
           return eventRepository.save(event);
        }
        return event;
    }

    @Override
    public Event getEventSt(Long eventId) {
        Event event = getEvent(eventId);
        if (event.getViews() == null) event.setViews(0L);
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        return event;
    }

/*
    //Господи, что я творю!!!
    @Override
    public Event patchEvent(Long eventId, UpdateEventUserRequest updateEventAdminRequest) {
        Event event = getEvent(eventId);
        if (event.getState().equals(State.PUBLISHED) || event.getState().equals(State.REJECTED)) {
            throw new ConflictException("The event had been published");
        }
        boolean check = checkEvent(event);


        return null;
    }*/

    @Override
    public Event patchEventByUser(Long eventId, Long userId, UpdateEventAdminRequest upd) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id:" + userId + " is absent.")
        );
        Event event = getEvent(eventId);

        if(!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotAvailableException("Event with id:" + eventId + " cannot  be edited by user with id:" + userId);
        }

        /*if (!upd.getStateAction().equals("CANCEL_REVIEW") &&  !upd.getStateAction().equals("PUBLISH_EVENT")) {
           throw new ConflictException("Forbidden operation");
        }*/

        if (!event.getState().equals(State.PUBLISHED)) {
            updateFields(upd, event);
            return eventRepository.save(event);
        } else {
            throw new ConflictException("Forbidden operation");
        }
    }

    @Override
    public List<Event> searchEvents(String query, List<Long> categoryIds, Boolean pais, LocalDateTime start,
                                            LocalDateTime end, Boolean onlyAvailable, String sort, Integer from,
                                            Integer size) {
        Pageable pageable = PageRequest.of(from/size, size);
        List<Event> eventList;
        List<Event> sortList = new ArrayList<>();
        if (start != null && end != null) {
            if (end.isBefore(start)) {
                throw new NotAvailableException("Incorrect time range");
            }
        }


        if (start == null && end == null) {
            eventList = eventRepository.searchAllByAnnotationAndCategoryIdInAndStateIsAndEventDateIsAfter
                    (query, categoryIds, State.PUBLISHED, LocalDateTime.now(), pageable);
            if (eventList.size() == 0) {
                eventList = eventRepository
                        .searchAllByDescriptionAndCategoryIdInAndStateIsAndEventDateIsAfter
                                (query, categoryIds, State.PUBLISHED, LocalDateTime.now(), pageable);
            }
        } else {
            eventList = eventRepository
                    .searchAllByAnnotationAndCategoryIdInAndStateIsAndEventDateIsAfterAndEventDateIsBefore
                            (query, categoryIds, State.PUBLISHED, start, end, pageable);
            if (eventList.size() == 0) {
                eventList = eventRepository
                        .searchAllByDescriptionAndCategoryIdInAndStateIsAndEventDateIsAfterAndEventDateIsBefore
                                (query, categoryIds, State.PUBLISHED, start, end, pageable);
            }
        }



        if ((Boolean) pais != null) {
            if (pais) {
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

        if ((Boolean) onlyAvailable != null) {
            if (onlyAvailable) {
                for (Event event : eventList) {
                    if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                        sortList.add(event);
                    }
                }
                eventList.clear();
                eventList.addAll(sortList);
            }
        }
        return eventList;
    }


    @Override
    public void deleteEvent(Long id) {
        getEvent(id);
        eventRepository.deleteById(id);
    }

    private boolean checkEvent(Event event) {
        //boolean check = false;
        if (event.getState().equals(State.CANCELED)
                || event.getState().equals(State.PENDING)) {
            if (event.getEventDate().isAfter(LocalDateTime.now().plusHours(1))) {
                //check = true;
                return true;
            }
        }
        return false;
    }

    private void updateFields( UpdateEventAdminRequest upd, Event event) {
        Category newCategory = event.getCategory();
        checkUpdatedTime(upd);

        if (upd.getCategory() != null) {
             newCategory = categoryRepository.findById(upd.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category with id" + upd.getCategory() + "is absent"));
        }
        String stateAction = upd.getStateAction();

        if (stateAction != null) {
            if (stateAction.equals("CANCEL_REVIEW")) event.setState(State.CANCELED);
            if (stateAction.equals("PUBLISH_EVENT")) event.setState(State.PUBLISHED);
            if (stateAction.equals("REJECT_EVENT")) event.setState(State.REJECTED);
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

            if ((Boolean) upd.isPaid() != null) event.setPaid(upd.isPaid());

            if (upd.getParticipantLimit() != null) event.setParticipantLimit(upd.getParticipantLimit());

            if ((Boolean) upd.isRequestModeration() != null) event.setRequestModeration(upd.isRequestModeration());

            if (upd.getParticipantLimit() != null) event.setParticipantLimit(event.getParticipantLimit());

            if (upd.getTitle() != null) event.setTitle(event.getTitle());
        }
    }

    private static void checkUpdatedTime(UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getEventDate() != null) {
            if (LocalDateTime.now().plusHours(1).isAfter(updateEventAdminRequest.getEventDate())) {
                throw new NotAvailableException("Event time is too late");
            }
        }
    }
}