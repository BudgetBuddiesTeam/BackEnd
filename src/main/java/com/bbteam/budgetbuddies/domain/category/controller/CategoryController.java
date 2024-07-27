package com.bbteam.budgetbuddies.domain.category.controller;

import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDTO;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @Override
    @PostMapping("/add")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO response = categoryService.createCategory(categoryRequestDTO);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/get/{userId}")
    public ResponseEntity<List<CategoryResponseDTO>> getUserCategories(@PathVariable Long userId) {
        List<CategoryResponseDTO> response = categoryService.getUserCategories(userId);
        return ResponseEntity.ok(response);
    }
}
