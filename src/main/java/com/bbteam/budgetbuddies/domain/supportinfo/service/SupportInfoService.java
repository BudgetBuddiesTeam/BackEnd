package com.bbteam.budgetbuddies.domain.supportinfo.service;

import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportRequest;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import org.springframework.data.domain.Page;

public interface SupportInfoService {
    Page<SupportResponseDto> getSupportsByYearAndMonth(
        Integer year,
        Integer month,
        Integer page,
        Integer size
    );

    SupportResponseDto registerSupportInfo(SupportRequest.RegisterSupportDto supportRequest);

    SupportResponseDto toggleLike(Long userId, Long supportInfoId);

    SupportResponseDto updateSupportInfo(SupportRequest.UpdateSupportDto supportRequestDto);

    String deleteSupportInfo(Long supportInfoId);

    SupportResponseDto getSupportInfoById(Long supportInfoId);

}