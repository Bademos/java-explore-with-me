package ru.practicum.mainservice.models.request;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestMapper {
    public RequestDto makeRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .requester(request.getRequester().getId())
                .status(request.getStatus().toString())
                .build();
    }
}