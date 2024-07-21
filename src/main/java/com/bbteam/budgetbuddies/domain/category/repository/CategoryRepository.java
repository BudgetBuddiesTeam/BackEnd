package com.bbteam.budgetbuddies.domain.category.repository;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByUserIdAndName(Long userId, String name);
}

