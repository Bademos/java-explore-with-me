package ru.practicum.mainservice.service;

import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.category.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories(int from, int size);

    Category getCategoryById(Long id);

    Category updateCategory(CategoryDto category, Long catId);

    Category addCategory(Category category);

    void deleteCategory(Long id);
}
