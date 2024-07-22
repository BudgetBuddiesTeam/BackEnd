package com.bbteam.budgetbuddies.domain.category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDTO {
    private Long userId;
    private String name;
    private Boolean isDefault;
}
