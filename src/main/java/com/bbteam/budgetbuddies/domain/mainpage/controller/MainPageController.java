package com.bbteam.budgetbuddies.domain.mainpage.controller;

import com.bbteam.budgetbuddies.domain.mainpage.dto.MainPageResponseDto;
import com.bbteam.budgetbuddies.domain.mainpage.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainPageController implements MainPageApi{

    private final MainPageService mainPageService;

    @GetMapping("/main")
    public ResponseEntity<MainPageResponseDto> getMainPage
            (@RequestParam("userId") Long userId) {
        MainPageResponseDto mainPage = mainPageService.getMainPage(userId);
        return ResponseEntity.ok(mainPage);
    }
}
