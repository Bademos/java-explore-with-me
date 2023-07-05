package ru.practicum.mainservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.MainConstantShare;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.models.compilation.Compilation;
import ru.practicum.mainservice.models.compilation.dto.CompilationDtoUpd;
import ru.practicum.mainservice.models.event.Event;
import ru.practicum.mainservice.repository.CompilationRepository;
import ru.practicum.mainservice.repository.EventRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CompilationServiceImpl implements CompilationService {
    CompilationRepository compilationRepository;
    EventRepository eventRepository;

    @Override
    public List<Compilation> getAllCompilation(Boolean pinned, int from, int size) {

        PageRequest pg = pinned == null
                ? PageRequest.of(from/size, size)
                : PageRequest.of(from/size, size, MainConstantShare.SORT_ASC);

        return compilationRepository.findAll(pg).toList();
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
    public Compilation updateCompilation(CompilationDtoUpd compilationDtoIn, Long compId) {
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
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation deleteCompilation(Long id) {
        Compilation compilation = getCompilationById(id);
        compilationRepository.deleteById(id);
        return compilation;
    }
}