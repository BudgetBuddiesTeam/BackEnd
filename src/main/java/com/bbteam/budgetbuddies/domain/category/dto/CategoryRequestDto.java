package com.bbteam.budgetbuddies.domain.category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {
    private String name;
    private Boolean isDefault;
}
