package ru.practicum.mainservice.controllers.publique;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.models.category.dto.CategoryDto;
import ru.practicum.mainservice.models.category.MakeCategory;
import ru.practicum.mainservice.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping("/categories")
@Validated
public class PublicCategoriesController {
    CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Got request for all categories");
        return categoryService.getCategories(from, size).stream()
                .map(MakeCategory::makeDtoFromCategory)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable @Positive Long id) {
        log.info("Request for category with id:{}", id);
        return MakeCategory.makeDtoFromCategory(categoryService.getCategoryById(id));
    }
}