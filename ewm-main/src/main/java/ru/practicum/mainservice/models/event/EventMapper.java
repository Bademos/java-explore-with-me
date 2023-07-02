package ru.practicum.mainservice.models.event;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.models.category.MakeCategory;
import ru.practicum.mainservice.models.event.dto.EventFullDto;
import ru.practicum.mainservice.models.event.dto.EventShortDto;
import ru.practicum.mainservice.models.event.dto.NewEventDto;
import ru.practicum.mainservice.models.location.Location;
import ru.practicum.mainservice.models.location.LocationDto;
import ru.practicum.mainservice.models.user.UserMapper;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {
    public Event makeEventFromDto(NewEventDto newEventDto) {
        return Event.builder()
                .state(State.PENDING)
                .createdOn(LocalDateTime.now())
                .eventDate(newEventDto.getEventDate())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .paid(newEventDto.isPaid())
                .location(makeLocationFromLocationDto(newEventDto.getLocation()))
                .title(newEventDto.getTitle())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .build();

    }

    public EventFullDto makeFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(MakeCategory.makeDtoFromCategory(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(makeDtoFromLocation(event.getLocation()))
                .initiator(UserMapper.makeUserShortDtoFromUser(event.getInitiator()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .views(event.getViews())
                .title(event.getTitle())
                .build();
    }

    public EventShortDto makeShortDto(Event event) {
        return EventShortDto.builder()
                .eventDate(event.getEventDate())
                .id(event.getId())
                .title(event.getTitle())
                .views(event.getViews())
                .annotation(event.getAnnotation())
                .category(MakeCategory.makeDtoFromCategory(event.getCategory()))
                .paid(event.getPaid())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(UserMapper.makeUserShortDtoFromUser(event.getInitiator()))
                .build();
    }

    public LocationDto makeDtoFromLocation(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public Location makeLocationFromLocationDto(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}
