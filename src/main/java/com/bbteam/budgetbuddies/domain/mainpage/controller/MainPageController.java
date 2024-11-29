package com.bbteam.budgetbuddies.domain.mainpage.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.mainpage.dto.MainPageResponseDto;
import com.bbteam.budgetbuddies.domain.mainpage.service.MainPageService;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.global.security.utils.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class MainPageController implements MainPageApi{

    private final MainPageService mainPageService;

    @GetMapping("/main")
    public ApiResponse<MainPageResponseDto> getMainPage
            (@AuthUser UserDto.AuthUserDto userDto) {
        MainPageResponseDto mainPage = mainPageService.getMainPage(userDto.getId());
        return ApiResponse.onSuccess(mainPage);
    }
}
