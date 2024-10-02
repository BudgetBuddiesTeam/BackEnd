package com.bbteam.budgetbuddies.domain.faq.converter;

import com.bbteam.budgetbuddies.domain.faq.dto.FaqRequestDto;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqResponseDto;
import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import com.bbteam.budgetbuddies.domain.user.entity.User;

public class FaqConverter {

    public static Faq postToEntity(FaqRequestDto.FaqPostRequest dto, User user) {
        return Faq.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
                .user(user)
                .build();
    }

    public static FaqResponseDto.FaqPostResponse entityToPost(Faq faq) {
        return FaqResponseDto.FaqPostResponse.builder()
                .title(faq.getTitle())
                .body(faq.getBody())
                .faqId(faq.getId())
                .username(faq.getUser().getName())
                .build();
    }

    public static FaqResponseDto.FaqModifyResponse entityToModify(Faq faq) {
        return FaqResponseDto.FaqModifyResponse.builder()
                .title(faq.getTitle())
                .body(faq.getBody())
                .faqId(faq.getId())
                .username(faq.getUser().getName())
                .build();
    }

    public static FaqResponseDto.FaqFindResponse entityToFind(Faq faq) {
        return FaqResponseDto.FaqFindResponse.builder()
                .title(faq.getTitle())
                .body(faq.getBody())
                .username(faq.getUser().getName())
                .faqId(faq.getId())
                .userId(faq.getUser().getId())
                .build();
    }

}
