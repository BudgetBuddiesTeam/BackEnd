package com.bbteam.budgetbuddies.domain.faq.service;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.faq.converter.FaqConverter;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqRequestDto;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqResponseDto;
import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import com.bbteam.budgetbuddies.domain.faq.repository.FaqRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqServiceImpl implements FaqService{

    private final FaqRepository faqRepository;
    private final UserRepository userRepository;

    @Override
    public FaqResponseDto.FaqFindResponse findOneFaq(Long faqId) {
        Faq faq = findFaq(faqId);
        return FaqConverter.entityToFind(faq);
    }

    @Override
    public Page<FaqResponseDto.FaqFindResponse> findAllWithPaging(Pageable pageable) {
        return faqRepository.findAll(pageable).map(FaqConverter::entityToFind);
    }

    @Override
    @Transactional
    public FaqResponseDto.FaqPostResponse postFaq(FaqRequestDto.FaqPostRequest dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        Faq faq = FaqConverter.postToEntity(dto, user);
        faqRepository.save(faq);
        return FaqConverter.entityToPost(faq);
    }

    @Override
    @Transactional
    public FaqResponseDto.FaqModifyResponse modifyFaq(FaqRequestDto.FaqModifyRequest dto, Long faqId) {
        Faq faq = findFaq(faqId);
        faq.update(dto);
        return FaqConverter.entityToModify(faq);
    }

    @Override
    @Transactional
    public String deleteFaq(Long faqId) {
        Faq faq = findFaq(faqId);
        faqRepository.delete(faq);
        return "ok!";
    }

    private Faq findFaq(Long faqId) {
        return faqRepository.findById(faqId).orElseThrow(() -> new GeneralException(ErrorStatus._FAQ_NOT_FOUND));
    }
}
