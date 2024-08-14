package com.bbteam.budgetbuddies.domain.category.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponseDTO {
    private Long id;
    private Long userId;
    private String name;
    private Boolean isDefault;
}
