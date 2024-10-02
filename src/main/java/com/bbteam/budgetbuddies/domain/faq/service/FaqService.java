package com.bbteam.budgetbuddies.domain.faq.service;

import com.bbteam.budgetbuddies.domain.faq.dto.FaqRequestDto;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaqService {

    FaqResponseDto.FaqFindResponse findOneFaq(Long faqId);

    Page<FaqResponseDto.FaqFindResponse> findAllWithPaging(Pageable pageable);

    FaqResponseDto.FaqPostResponse postFaq(FaqRequestDto.FaqPostRequest dto, Long userId);

    FaqResponseDto.FaqModifyResponse modifyFaq(FaqRequestDto.FaqModifyRequest dto, Long faqId);

    String deleteFaq(Long faqId);
}
