package com.bbteam.budgetbuddies.domain.supportinfo.service;

import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequestDto;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportRequestDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import org.springframework.data.domain.Page;

public interface SupportInfoService {
    Page<SupportResponseDto> getSupportsByYearAndMonth(
        Integer year,
        Integer month,
        Integer page,
        Integer size
    );

    SupportResponseDto registerSupportInfo(SupportRequestDto supportRequestDto);

    SupportResponseDto toggleLike(Long userId, Long supportInfoId);
}
