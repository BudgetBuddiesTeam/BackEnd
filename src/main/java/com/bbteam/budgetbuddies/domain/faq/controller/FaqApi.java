package com.bbteam.budgetbuddies.domain.faq.controller;


import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqRequestDto;
import com.bbteam.budgetbuddies.domain.faq.validation.ExistFaq;
import com.bbteam.budgetbuddies.domain.user.validation.ExistUser;
import org.springframework.data.domain.Pageable;

public interface FaqApi {
    ApiResponse<?> postFaq(@ExistUser Long userId, FaqRequestDto.FaqPostRequest dto);
    ApiResponse<?> findFaq(@ExistFaq Long FaqId);
    ApiResponse<?> findByPaging(Pageable pageable);
    ApiResponse<?> modifyFaq(@ExistFaq Long faqId, FaqRequestDto.FaqModifyRequest dto);
    ApiResponse<?> deleteFaq(@ExistFaq Long faqId);
}
