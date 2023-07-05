package ru.practicum.mainservice.models.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.models.compilation.dto.CompilationDtoIn;
import ru.practicum.mainservice.models.compilation.dto.CompilationDtoOut;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.models.event.EventMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public Compilation makeCompilationFromDto(CompilationDtoIn compilationDtoIn, Set<Event> eventSets) {
        return Compilation.builder()
                .pinned(compilationDtoIn.isPinned())
                .title(compilationDtoIn.getTitle())
                .events(eventSets)
                .build();
    }

    public CompilationDtoOut makeDtoFromCompilation(Compilation compilation) {
        return CompilationDtoOut.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream().map(EventMapper::makeShortDto).collect(Collectors.toList()))
                .build();
    }
}