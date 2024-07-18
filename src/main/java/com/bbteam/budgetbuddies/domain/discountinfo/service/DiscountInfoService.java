package com.bbteam.budgetbuddies.domain.discountinfo.service;

import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequestDto;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import org.springframework.data.domain.Page;

public interface DiscountInfoService {
    Page<DiscountResponseDto> getDiscountsByYearAndMonth(
        Integer year,
        Integer month,
        Integer page,
        Integer size
    );

    DiscountResponseDto registerDiscountInfo(DiscountRequestDto discountRequestDto);

    DiscountResponseDto toggleLike(Long userId, Long discountInfoId);


}
