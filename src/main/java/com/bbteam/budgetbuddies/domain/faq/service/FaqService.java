package com.bbteam.budgetbuddies.domain.faq.service;

import com.bbteam.budgetbuddies.domain.faq.dto.FaqRequestDto;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqResponseDto;
import com.bbteam.budgetbuddies.domain.faqkeyword.dto.FaqKeywordResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaqService {

    FaqResponseDto.FaqFindResponse findOneFaq(Long faqId);

    Page<FaqResponseDto.FaqFindResponse> findAllWithPaging(Pageable pageable);

    Page<FaqResponseDto.FaqFindResponse> searchFaq(Pageable pageable, String searchCondition);

    FaqResponseDto.FaqPostResponse postFaq(FaqRequestDto.FaqPostRequest dto, Long userId);

    FaqResponseDto.FaqModifyResponse modifyFaq(FaqRequestDto.FaqModifyRequest dto, Long faqId);

    String deleteFaq(Long faqId);

    FaqKeywordResponseDto addKeyword(Long faqId, Long searchKeywordId);

    String removeKeyword(Long faqId, Long searchKeywordId);
}
