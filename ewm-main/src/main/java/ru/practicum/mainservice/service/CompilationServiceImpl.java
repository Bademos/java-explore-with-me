package ru.practicum.mainservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.models.compilation.Compilation;
import ru.practicum.mainservice.models.compilation.CompilationDtoIn;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.repository.CompilationRepository;
import ru.practicum.mainservice.repository.EventRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CompilationServiceImpl implements CompilationService{
    CompilationRepository compilationRepository;
    EventRepository eventRepository;

    @Override
    public List<Compilation> getAllCompilation(Boolean pinned, int from, int size) {
        PageRequest pg = PageRequest.of(from/size, size, Sort.by(Sort.Direction.ASC, "id"));

        var pageRequest = pinned == null
                ? PageRequest.of(from/size, size, Sort.unsorted())
                : PageRequest.of(from/size, size, Sort.by(Sort.Direction.ASC, "id"));

        return compilationRepository.findAll(pg).toList();
        //return compilationRepository.findAllByPinned(pinned, pageRequest);
        //return compilationRepository.findAllByPinned(pinned, pg);
    }

    @Override
    public Compilation getCompilationById(Long id) {
        return compilationRepository.findById(id).orElseThrow(
        () -> new NotFoundException("There is no compilation with id:{}" + id)
        );
    }

    @Override
    public Compilation addCompilation(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation updateCompilation(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation updateCompilation(CompilationDtoIn compilationDtoIn, Long compId) {
        Compilation compilation = getCompilationById(compId);
        if (compilationDtoIn.getEvents() != null) {
            Set<Event> events = eventRepository.findAllByIdIn(compilationDtoIn.getEvents());
            compilation.setEvents(events);
        }
        if (compilationDtoIn.getPinned() != null) {
            compilation.setPinned(compilationDtoIn.getPinned());
        }
        if (compilationDtoIn.getTitle() != null && !compilationDtoIn.getTitle().isBlank()) {
            compilation.setTitle(compilationDtoIn.getTitle());
        }
        /*if (compilationDtoIn.getEvents() != null) {
            compilation.setEvents(events);
        }*/
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation deleteCompilation(Long id) {
        Compilation compilation = getCompilationById(id);
        compilationRepository.deleteById(id);
        return compilation;
    }
}