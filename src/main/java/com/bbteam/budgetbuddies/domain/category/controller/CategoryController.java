package com.bbteam.budgetbuddies.domain.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDTO;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController implements CategoryApi {

	private final CategoryService categoryService;

	@Override
	@PostMapping("/add/{userId}")
	public ResponseEntity<CategoryResponseDTO> createCategory(
		@PathVariable Long userId,
		@RequestBody CategoryRequestDTO categoryRequestDTO) {
		CategoryResponseDTO response = categoryService.createCategory(userId, categoryRequestDTO);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping("/get/{userId}")
	public ResponseEntity<List<CategoryResponseDTO>> getUserCategories(@PathVariable Long userId) {
		List<CategoryResponseDTO> response = categoryService.getUserCategories(userId);
		return ResponseEntity.ok(response);
	}
}
