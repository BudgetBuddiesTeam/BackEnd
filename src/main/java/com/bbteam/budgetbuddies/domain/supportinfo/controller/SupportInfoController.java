package com.bbteam.budgetbuddies.domain.supportinfo.controller;

import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequest;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportRequest;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.service.SupportInfoService;
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
    public ResponseEntity<SupportResponseDto> registerSupportInfo(
        @RequestBody SupportRequest.RegisterDto requestDto
    ) {
        SupportResponseDto supportResponseDto = supportInfoService.registerSupportInfo(requestDto);

        return ResponseEntity.ok(supportResponseDto);
    }

    @Override
    @PostMapping("/likes/{supportInfoId}")
    public ResponseEntity<SupportResponseDto> likeSupportInfo(
        @RequestParam Long userId,
        @PathVariable Long supportInfoId
    ) {
        SupportResponseDto supportResponseDto = supportInfoService.toggleLike(userId, supportInfoId);

        return ResponseEntity.ok(supportResponseDto);
    }

    @Override
    @PutMapping("")
    public ResponseEntity<SupportResponseDto> updateSupportInfo(
        @RequestParam Long userId,
        @RequestBody SupportRequest.UpdateDto supportRequestDto
    ) {
        SupportResponseDto supportResponseDto = supportInfoService.updateSupportInfo(userId, supportRequestDto);

        return ResponseEntity.ok(supportResponseDto);
    }

    @Override
    @DeleteMapping("/{supportInfoId}")
    public ResponseEntity<String> deleteSupportInfo(
        @RequestParam Long userId,
        @PathVariable Long supportInfoId
    ) {
        String message = supportInfoService.deleteSupportInfo(userId, supportInfoId);

        return ResponseEntity.ok(message);
    }

    @Override
    @GetMapping("/{supportInfoId}")
    public ResponseEntity<SupportResponseDto> getSupportInfo(
        @RequestParam Long userId,
        @PathVariable Long supportInfoId
    ) {
        SupportResponseDto supportResponseDto = supportInfoService.getSupportInfoById(userId, supportInfoId);

        return ResponseEntity.ok(supportResponseDto);
    }

}
