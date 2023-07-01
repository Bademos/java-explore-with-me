package ru.practicum.mainservice.service;

import ru.practicum.mainservice.models.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.models.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.models.request.Request;

import java.util.List;

public interface RequestService {
    List<Request> getAllByUser(Long userId);

    EventRequestStatusUpdateResult patchRequestsByUserIdAndEventId(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);


    List<Request> getAllByEventAndUser(Long eventId, Long userId);

    Request cancelRequest(Long requestId, Long userId);

    Request addRequest(Long eventId, Long userId);
}
