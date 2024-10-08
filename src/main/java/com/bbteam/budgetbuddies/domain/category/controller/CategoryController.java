package com.bbteam.budgetbuddies.domain.category.controller;

import java.util.List;

import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDto;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bbteam.budgetbuddies.domain.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController implements CategoryApi {

	private final CategoryService categoryService;

	@Override
	@PostMapping("/add/{userId}")
	public ResponseEntity<CategoryResponseDto> createCategory(
		@PathVariable Long userId,
		@RequestBody CategoryRequestDto categoryRequestDTO) {
		CategoryResponseDto response = categoryService.createCategory(userId, categoryRequestDTO);
		return ResponseEntity.ok(response);
	}

    @Override
    @GetMapping("/get/{userId}")
    public ResponseEntity<List<CategoryResponseDto>> getUserCategories(@PathVariable Long userId) {
        List<CategoryResponseDto> response = categoryService.getUserCategories(userId);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @RequestParam Long userId,
            @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId, userId);
        return ResponseEntity.ok("Successfully deleted category!");
    }
}
