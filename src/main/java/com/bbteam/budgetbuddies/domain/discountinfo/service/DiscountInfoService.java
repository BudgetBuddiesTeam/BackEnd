package com.bbteam.budgetbuddies.domain.discountinfo.service;

import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequest;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import org.springframework.data.domain.Page;

public interface DiscountInfoService {
    Page<DiscountResponseDto> getDiscountsByYearAndMonth(
        Integer year,
        Integer month,
        Integer page,
        Integer size
    );

    DiscountResponseDto registerDiscountInfo(DiscountRequest.RegisterDiscountDto discountRequestDto);

    DiscountResponseDto toggleLike(Long userId, Long discountInfoId);

    DiscountResponseDto updateDiscountInfo(DiscountRequest.UpdateDiscountDto discountRequestDto);

    String deleteDiscountInfo(Long discountInfoId);

    DiscountResponseDto getDiscountInfoById(Long discountInfoId);

    Page<DiscountResponseDto> getLikedDiscountInfo(
        Long userId,
        Integer page,
        Integer size
    );

}