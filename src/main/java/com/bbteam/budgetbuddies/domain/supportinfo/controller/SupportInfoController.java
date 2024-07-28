package com.bbteam.budgetbuddies.domain.supportinfo.controller;

import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequestDto;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportRequestDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.service.SupportInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supports")
public class SupportInfoController implements SupportInfoApi {

    private final SupportInfoService supportInfoService;

    @Override
    @GetMapping("")
    public ResponseEntity<Page<SupportResponseDto>> getSupportsByYearAndMonth(
        @RequestParam Integer year,
        @RequestParam Integer month,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<SupportResponseDto> supports = supportInfoService.getSupportsByYearAndMonth(year, month, page, size);

        return ResponseEntity.ok(supports);
    }

    @Override
    @PostMapping("")
    public ResponseEntity<SupportResponseDto> registerDiscountInfo(
        @RequestBody SupportRequestDto requestDto
    ) {
        SupportResponseDto supportResponseDto = supportInfoService.registerSupportInfo(requestDto);

        return ResponseEntity.ok(supportResponseDto);
    }

    @Override
    @PostMapping("/{supportInfoId}/likes")
    public ResponseEntity<SupportResponseDto> likeDiscountInfo(
        @RequestParam Long userId,
        @PathVariable Long supportInfoId
    ) {
        SupportResponseDto supportResponseDto = supportInfoService.toggleLike(userId, supportInfoId);

        return ResponseEntity.ok(supportResponseDto);
    }

}
