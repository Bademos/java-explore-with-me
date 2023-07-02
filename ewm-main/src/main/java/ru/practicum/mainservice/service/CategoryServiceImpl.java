package ru.practicum.mainservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exceptions.ConflictException;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.category.dto.CategoryDto;
import ru.practicum.mainservice.repository.CategoryRepository;
import ru.practicum.mainservice.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    EventRepository eventRepository;

    @Override
    public List<Category> getCategories(int from, int size) {
        from /= size;
        PageRequest pr = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "id"));
        return categoryRepository.findAll(pr).toList();
    }

    @Override
    public Category getCategoryById(Long id) {
        try {
            return categoryRepository.findById(id).orElseThrow();
        } catch (RuntimeException e) {
            throw new NotFoundException("There are no the category in DB");
        }
    }

    @Override
    public Category updateCategory(CategoryDto category, Long catId) {
        if (categoryRepository.existsByIdNotAndName(catId, category.getName())) {
            throw new ConflictException("Category with the name already exist");
        }
        Category categoryToSave = getCategoryById(catId);
        categoryToSave.setName(category.getName());

        return categoryRepository.save(categoryToSave);
    }

    @Override
    public Category addCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("The name already used");
        }
    }

    @Override
    public void deleteCategory(Long id) {
        getCategoryById(id);
        if (!eventRepository.findAllByCategoryId(id).isEmpty()) {
            throw new ConflictException("There is events with that cathegory");
        }
        categoryRepository.deleteById(id);
    }
}
