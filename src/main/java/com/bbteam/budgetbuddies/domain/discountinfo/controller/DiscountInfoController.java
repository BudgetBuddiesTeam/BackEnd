package com.bbteam.budgetbuddies.domain.discountinfo.controller;

import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequest;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.service.DiscountInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discounts")
public class DiscountInfoController implements DiscountInfoApi {

    private final DiscountInfoService discountInfoService;

    @Override
    @GetMapping("")
    public ResponseEntity<Page<DiscountResponseDto>> getDiscountsByYearAndMonth(
        @RequestParam Integer year,
        @RequestParam Integer month,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<DiscountResponseDto> discounts = discountInfoService.getDiscountsByYearAndMonth(year, month, page, size);

        return ResponseEntity.ok(discounts);
    }

    @Override
    @PostMapping("")
    public ResponseEntity<DiscountResponseDto> registerDiscountInfo(
        @RequestBody DiscountRequest.RegisterDto discountRequestDto
    ) {
        DiscountResponseDto discountResponseDto = discountInfoService.registerDiscountInfo(discountRequestDto);

        return ResponseEntity.ok(discountResponseDto);
    }

    @Override
    @PostMapping("/likes/{discountInfoId}")
    public ResponseEntity<DiscountResponseDto> likeDiscountInfo(
        @RequestParam Long userId,
        @PathVariable Long discountInfoId
    ) {
        DiscountResponseDto discountResponseDto = discountInfoService.toggleLike(userId, discountInfoId);

        return ResponseEntity.ok(discountResponseDto);
    }

    @Override
    @PutMapping("")
    public ResponseEntity<DiscountResponseDto> updateDiscountInfo(
        @RequestParam Long userId,
        @RequestBody DiscountRequest.UpdateDto discountRequestDto
    ) {
        DiscountResponseDto discountResponseDto = discountInfoService.updateDiscountInfo(userId, discountRequestDto);

        return ResponseEntity.ok(discountResponseDto);
    }

    @Override
    @DeleteMapping("{discountInfoId}")
    public ResponseEntity<String> deleteDiscountInfo(
        @RequestParam Long userId,
        @PathVariable Long discountInfoId
    ) {
        String message = discountInfoService.deleteDiscountInfo(userId, discountInfoId);

        return ResponseEntity.ok(message);
    }

}
