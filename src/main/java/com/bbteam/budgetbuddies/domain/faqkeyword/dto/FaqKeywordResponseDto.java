package com.bbteam.budgetbuddies.domain.faqkeyword.dto;

import com.bbteam.budgetbuddies.domain.faqkeyword.domain.FaqKeyword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FaqKeywordResponseDto {

    private Long faqId;
    private Long searchKeywordId;

    private String faqTitle;
    private String keyword;

    public static FaqKeywordResponseDto toDto(FaqKeyword faqKeyword) {
        return new FaqKeywordResponseDto(faqKeyword.getFaq().getId(), faqKeyword.getSearchKeyword().getId(),
                faqKeyword.getFaq().getTitle(), faqKeyword.getSearchKeyword().getKeyword());
    }
}
