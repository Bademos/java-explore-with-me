package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.models.category.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByIdNotAndName(long catId, String name);
}
