package com.bbteam.budgetbuddies.domain.favoritehashtag.dto;

import com.bbteam.budgetbuddies.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class FavoriteHashtagResponseDto {
    private Long userId;

    public FavoriteHashtagResponseDto(User user) {
        this.userId = user.getId();
    }
}
