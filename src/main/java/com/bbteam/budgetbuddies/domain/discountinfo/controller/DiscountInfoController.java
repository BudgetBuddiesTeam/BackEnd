package com.bbteam.budgetbuddies.domain.discountinfo.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequest;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.service.DiscountInfoService;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.global.security.utils.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discounts")
public class DiscountInfoController implements DiscountInfoApi {

    private final DiscountInfoService discountInfoService;

    @Override
    @GetMapping("")
    public ApiResponse<Page<DiscountResponseDto>> getDiscountsByYearAndMonth(
        @RequestParam Integer year,
        @RequestParam Integer month,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<DiscountResponseDto> discounts = discountInfoService.getDiscountsByYearAndMonth(year, month, page, size);

        return ApiResponse.onSuccess(discounts);
    }

    @Override
    @PostMapping("")
    public ApiResponse<DiscountResponseDto> registerDiscountInfo(
        @RequestBody DiscountRequest.RegisterDiscountDto discountRequestDto
    ) {
        DiscountResponseDto discountResponseDto = discountInfoService.registerDiscountInfo(discountRequestDto);

        return ApiResponse.onSuccess(discountResponseDto);
    }

    @Override
    @PostMapping("/likes/{discountInfoId}")
    public ApiResponse<DiscountResponseDto> likeDiscountInfo(
        @AuthUser UserDto.AuthUserDto user,
        @PathVariable Long discountInfoId
    ) {
        DiscountResponseDto discountResponseDto = discountInfoService.toggleLike(user.getId(), discountInfoId);

        return ApiResponse.onSuccess(discountResponseDto);
    }

    @Override
    @PutMapping("")
    public ApiResponse<DiscountResponseDto> updateDiscountInfo(
        @RequestBody DiscountRequest.UpdateDiscountDto discountRequestDto
    ) {
        DiscountResponseDto discountResponseDto = discountInfoService.updateDiscountInfo(discountRequestDto);

        return ApiResponse.onSuccess(discountResponseDto);
    }

    @Override
    @DeleteMapping("/{discountInfoId}")
    public ApiResponse<String> deleteDiscountInfo(
        @PathVariable Long discountInfoId
    ) {
        String message = discountInfoService.deleteDiscountInfo(discountInfoId);

        return ApiResponse.onSuccess(message);
    }


    @Override
    @GetMapping("/{discountInfoId}")
    public ApiResponse<DiscountResponseDto> getDiscountInfo(
        @PathVariable Long discountInfoId
    ) {
        DiscountResponseDto discountResponseDto = discountInfoService.getDiscountInfoById(discountInfoId);

        return ApiResponse.onSuccess(discountResponseDto);
    }

    @Override
    @GetMapping("/liked-all")
    public ApiResponse<Page<DiscountResponseDto>> getLikedDiscountInfo(
        @AuthUser UserDto.AuthUserDto user,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<DiscountResponseDto> likedDiscountInfoPage = discountInfoService.getLikedDiscountInfo(user.getId(), page, size);

        return ApiResponse.onSuccess(likedDiscountInfoPage);
    }

}
