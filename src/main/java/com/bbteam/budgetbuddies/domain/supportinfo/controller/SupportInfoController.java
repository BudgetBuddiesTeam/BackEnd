package com.bbteam.budgetbuddies.domain.supportinfo.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportRequest;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.service.SupportInfoService;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.domain.user.validation.ExistUser;
import com.bbteam.budgetbuddies.global.security.utils.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supports")
public class SupportInfoController implements SupportInfoApi {

    private final SupportInfoService supportInfoService;

    @Override
    @GetMapping("")
    public ApiResponse<Page<SupportResponseDto>> getSupportsByYearAndMonth(
        @RequestParam Integer year,
        @RequestParam Integer month,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<SupportResponseDto> supports = supportInfoService.getSupportsByYearAndMonth(year, month, page, size);

        return ApiResponse.onSuccess(supports);
    }

    @Override
    @PostMapping("")
    public ApiResponse<SupportResponseDto> registerSupportInfo(
        @RequestBody SupportRequest.RegisterSupportDto requestDto
    ) {
        SupportResponseDto supportResponseDto = supportInfoService.registerSupportInfo(requestDto);

        return ApiResponse.onSuccess(supportResponseDto);
    }

    @Override
    @PostMapping("/likes/{supportInfoId}")
    public ApiResponse<SupportResponseDto> likeSupportInfo(
        @AuthUser UserDto.AuthUserDto user,
        @PathVariable Long supportInfoId
    ) {
        SupportResponseDto supportResponseDto = supportInfoService.toggleLike(user.getId(), supportInfoId);

        return ApiResponse.onSuccess(supportResponseDto);
    }

    @Override
    @PutMapping("")
    public ApiResponse<SupportResponseDto> updateSupportInfo(
        @RequestBody SupportRequest.UpdateSupportDto supportRequestDto
    ) {
        SupportResponseDto supportResponseDto = supportInfoService.updateSupportInfo(supportRequestDto);

        return ApiResponse.onSuccess(supportResponseDto);
    }

    @Override
    @DeleteMapping("/{supportInfoId}")
    public ApiResponse<String> deleteSupportInfo(
        @PathVariable Long supportInfoId
    ) {
        String message = supportInfoService.deleteSupportInfo(supportInfoId);

        return ApiResponse.onSuccess(message);
    }

    @Override
    @GetMapping("/{supportInfoId}")
    public ApiResponse<SupportResponseDto> getSupportInfo(
        @PathVariable Long supportInfoId
    ) {
        SupportResponseDto supportResponseDto = supportInfoService.getSupportInfoById(supportInfoId);

        return ApiResponse.onSuccess(supportResponseDto);
    }

    @Override
    @GetMapping("/liked-all")
    public ApiResponse<Page<SupportResponseDto>> getLikedSupportInfo(
        @AuthUser UserDto.AuthUserDto user,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<SupportResponseDto> likedSupportInfoPage = supportInfoService.getLikedSupportInfo(user.getId(), page, size);

        return ApiResponse.onSuccess(likedSupportInfoPage);
    }
}
