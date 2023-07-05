package ru.practicum.mainservice.controllers.publique;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.models.compilation.dto.CompilationDtoOut;
import ru.practicum.mainservice.models.compilation.CompilationMapper;
import ru.practicum.mainservice.service.CompilationService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/compilations")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PublicCompilationController {
    CompilationService compilationService;

    @GetMapping("/{compId}")
    public CompilationDtoOut getCompilation(@PathVariable @Positive Long compId) {
        log.info("Got request for compilation with id:{}", compId);
        return CompilationMapper.makeDtoFromCompilation(compilationService.getCompilationById(compId));
    }

    @GetMapping
    public List<CompilationDtoOut> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                    @RequestParam(name = "from", defaultValue = "0") int from,
                                                    @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Got request for all compilations");
        return compilationService.getAllCompilation(pinned, from, size).stream()
                .map(CompilationMapper::makeDtoFromCompilation)
                .collect(Collectors.toList());
    }
}