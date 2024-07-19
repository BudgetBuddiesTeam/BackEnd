package com.bbteam.budgetbuddies.domain.category.service;

import com.bbteam.budgetbuddies.domain.category.converter.CategoryConverter;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDTO;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryConverter categoryConverter;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        User user = userRepository.findById(categoryRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("cannot find user"));

        categoryRepository.findByUserIdAndName(categoryRequestDTO.getUserId(), categoryRequestDTO.getName())
                .ifPresent(existingCategory -> {
                    throw new IllegalArgumentException("Category with the same name already exists for this user");
                });

        Category category = categoryConverter.toCategoryEntity(categoryRequestDTO, user);
        Category savedCategory = categoryRepository.save(category);

        return categoryConverter.toCategoryResponseDTO(savedCategory);
    }
}
