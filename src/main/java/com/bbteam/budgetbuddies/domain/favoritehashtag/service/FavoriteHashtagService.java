package com.bbteam.budgetbuddies.domain.favoritehashtag.service;

import com.bbteam.budgetbuddies.domain.favoritehashtag.dto.FavoriteHashtagResponseDto;

import java.util.List;

public interface FavoriteHashtagService {
    List<Long> getUsersForHashtag(Long discountInfoId, Long supportInfoId);

    List<FavoriteHashtagResponseDto> findUsersByHashtag(Long discountInfoId, Long supportInfoId);

}