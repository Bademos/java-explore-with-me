package ru.practicum.mainservice.service;

import ru.practicum.mainservice.models.compilation.Compilation;
import ru.practicum.mainservice.models.compilation.dto.CompilationDtoUpd;

import java.util.List;

public interface CompilationService {
    List<Compilation> getAllCompilation(Boolean pinnsed, int from, int size);

    Compilation getCompilationById(Long id);

    Compilation addCompilation(Compilation compilation);

    Compilation updateCompilation(Compilation compilation);

    Compilation updateCompilation(CompilationDtoUpd compilation, Long compId);

    Compilation deleteCompilation(Long id);
}
