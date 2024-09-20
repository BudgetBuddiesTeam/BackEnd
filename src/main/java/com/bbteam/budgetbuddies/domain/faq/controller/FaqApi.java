package com.bbteam.budgetbuddies.domain.faq.controller;


import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqRequestDto;
import org.springframework.data.domain.Pageable;

public interface FaqApi {
    ApiResponse<?> postFaq(Long userId, FaqRequestDto.FaqPostRequest dto);
    ApiResponse<?> findFaq(Long FaqId);
    ApiResponse<?> findByPaging(Pageable pageable);
    ApiResponse<?> modifyFaq(Long faqId, FaqRequestDto.FaqModifyRequest dto);
    ApiResponse<?> deleteFaq(Long faqId);
}
