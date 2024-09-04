package com.bbteam.budgetbuddies.domain.category.converter;

import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDto;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDto;
import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public Category toCategoryEntity(CategoryRequestDto categoryRequestDTO, User user) {
        return Category.builder()
                .name(categoryRequestDTO.getName())
                .isDefault(categoryRequestDTO.getIsDefault())
                .user(user)
                .deleted(false)
                .build();
    }

    public CategoryResponseDto toCategoryResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .userId(category.getUser().getId())
                .isDefault(category.getIsDefault())
                .build();
    }
}
