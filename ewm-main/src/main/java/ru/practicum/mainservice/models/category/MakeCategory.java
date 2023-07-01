package ru.practicum.mainservice.models.category;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MakeCategory {
    public Category makeCategoryFromNewDto(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public CategoryDto makeDtoFromCategory(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }


}
