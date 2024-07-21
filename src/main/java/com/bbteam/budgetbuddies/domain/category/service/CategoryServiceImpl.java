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
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryConverter categoryConverter;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        User user = userRepository.findById(categoryRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("cannot find user"));

        if (categoryRepository.existsByUserIdAndName(categoryRequestDTO.getUserId(), categoryRequestDTO.getName())) {
            throw new IllegalArgumentException("User already has a category with the same name");
        }


        Category category = categoryConverter.toCategoryEntity(categoryRequestDTO, user);
        Category savedCategory = categoryRepository.save(category);

        return categoryConverter.toCategoryResponseDTO(savedCategory);
    }
}