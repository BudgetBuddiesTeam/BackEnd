package com.bbteam.budgetbuddies.domain.favoritehashtag.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.favoritehashtag.dto.FavoriteHashtagResponseDto;
import com.bbteam.budgetbuddies.domain.favoritehashtag.service.FavoriteHashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favoriteHashtags")
public class FavoriteHashtagController implements FavoriteHashtagApi {

    private final FavoriteHashtagService favoriteHashtagService;

    @Override
    @GetMapping("/applicable-users")
    public ApiResponse<List<FavoriteHashtagResponseDto>> getUsersByHashtags(
            @RequestParam(required = false) Long discountInfoId,
            @RequestParam(required = false) Long supportInfoId
    ) {
        List<FavoriteHashtagResponseDto> users = favoriteHashtagService.findUsersByHashtag(discountInfoId, supportInfoId);
        return ApiResponse.onSuccess(users);
    }
}
