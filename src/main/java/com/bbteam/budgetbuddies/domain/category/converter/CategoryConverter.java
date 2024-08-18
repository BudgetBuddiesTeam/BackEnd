package com.bbteam.budgetbuddies.domain.category.converter;

import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDTO;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public Category toCategoryEntity(CategoryRequestDTO categoryRequestDTO, User user) {
        return Category.builder()
                .name(categoryRequestDTO.getName())
                .isDefault(categoryRequestDTO.getIsDefault())
                .user(user)
                .deleted(false)
                .build();
    }

    public CategoryResponseDTO toCategoryResponseDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .userId(category.getUser().getId())
                .isDefault(category.getIsDefault())
                .build();
    }
}
