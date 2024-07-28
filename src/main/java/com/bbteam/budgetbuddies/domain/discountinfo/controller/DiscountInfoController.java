package com.bbteam.budgetbuddies.domain.discountinfo.controller;

import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequestDto;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.service.DiscountInfoService;
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
        @RequestBody DiscountRequestDto discountRequestDto
    ) {
        DiscountResponseDto discountResponseDto = discountInfoService.registerDiscountInfo(discountRequestDto);

        return ResponseEntity.ok(discountResponseDto);
    }

    @Override
    @PostMapping("/{discountInfoId}/likes")
    public ResponseEntity<DiscountResponseDto> likeDiscountInfo(
        @RequestParam Long userId,
        @PathVariable Long discountInfoId
    ) {
        DiscountResponseDto discountResponseDto = discountInfoService.toggleLike(userId, discountInfoId);

        return ResponseEntity.ok(discountResponseDto);
    }

}
