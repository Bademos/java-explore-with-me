package ru.practicum.mainservice.controllers.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.models.compilation.Compilation;
import ru.practicum.mainservice.models.compilation.CompilationDtoIn;
import ru.practicum.mainservice.models.compilation.CompilationDtoOut;
import ru.practicum.mainservice.models.compilation.CompilationMapper;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.service.CompilationService;
import ru.practicum.mainservice.service.EventService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("admin/compilations")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminCompilationController {
    CompilationService compilationService;
    EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDtoOut addCompilation(@RequestBody @Valid CompilationDtoIn compilationDtoIn) {
        if(compilationDtoIn.getEvents() != null) {
            Set<Event> events = compilationDtoIn
                    .getEvents()
                    .stream()
                    .map(eventService::getEvent)
                    .collect(Collectors.toSet());
            Compilation compilation = CompilationMapper.makeCompilationFromDto(compilationDtoIn, events);
            return CompilationMapper.makeDtoFromCompilation(compilationService.addCompilation(compilation));
        }
        Compilation compilation = CompilationMapper.makeCompilationFromDto(compilationDtoIn, Collections.emptySet());
        return CompilationMapper.makeDtoFromCompilation(compilationService.addCompilation(compilation));
    }

    @PatchMapping("/{compId}")
    public CompilationDtoOut updateCompilation(@PathVariable Long compId,
                                               @RequestBody @Valid CompilationDtoIn compilationDtoIn) {
        return CompilationMapper.makeDtoFromCompilation(compilationService.updateCompilation(compilationDtoIn, compId));
    }
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }
}