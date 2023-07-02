package ru.practicum.mainservice.controllers.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.category.dto.CategoryDto;
import ru.practicum.mainservice.models.category.MakeCategory;
import ru.practicum.mainservice.models.category.dto.NewCategoryDto;
import ru.practicum.mainservice.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Validated
public class AdminCategoriesController {

    CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto сategoryDto) {
        Category category = MakeCategory.makeCategoryFromNewDto(сategoryDto);
        log.info("Got reguest to add new category:{}", сategoryDto);
        return MakeCategory.makeDtoFromCategory(categoryService.addCategory(category));
    }

    @PatchMapping("/{id}")
    public CategoryDto updateCategory(@Positive @PathVariable Long id, @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Got request for patching {}", categoryDto.getName());
        return MakeCategory.makeDtoFromCategory(categoryService.updateCategory(categoryDto, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        log.info("Got request to delete category with id:{}", id);
        categoryService.deleteCategory(id);
    }
}