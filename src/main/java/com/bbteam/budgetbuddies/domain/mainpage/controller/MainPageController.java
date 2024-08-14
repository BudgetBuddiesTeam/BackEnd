package com.bbteam.budgetbuddies.domain.mainpage.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.mainpage.dto.MainPageResponseDto;
import com.bbteam.budgetbuddies.domain.mainpage.service.MainPageService;
import com.bbteam.budgetbuddies.domain.user.validation.ExistUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class MainPageController implements MainPageApi{

    private final MainPageService mainPageService;

    @GetMapping("/main")
    public ApiResponse<MainPageResponseDto> getMainPage
            (@RequestParam("userId") @ExistUser Long userId) {
        MainPageResponseDto mainPage = mainPageService.getMainPage(userId);
        return ApiResponse.onSuccess(mainPage);
    }
}
