package com.bbteam.budgetbuddies.domain.category.dto;

import com.bbteam.budgetbuddies.domain.user.entity.User;
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
