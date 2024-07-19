package com.bbteam.budgetbuddies.domain.category.repository;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByUserIdAndName(Long userId, String name);
}

