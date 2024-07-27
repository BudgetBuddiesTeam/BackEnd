package com.bbteam.budgetbuddies.domain.mainpage.service;

import com.bbteam.budgetbuddies.domain.mainpage.dto.MainPageResponseDto;

public interface MainPageService {

    MainPageResponseDto getMainPage(Long userId);
}
