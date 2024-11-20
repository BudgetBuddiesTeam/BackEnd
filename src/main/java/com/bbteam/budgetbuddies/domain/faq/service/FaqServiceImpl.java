package com.bbteam.budgetbuddies.domain.faq.service;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.faq.converter.FaqConverter;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqRequestDto;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqResponseDto;
import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import com.bbteam.budgetbuddies.domain.faq.repository.FaqRepository;
import com.bbteam.budgetbuddies.domain.faqkeyword.domain.FaqKeyword;
import com.bbteam.budgetbuddies.domain.faqkeyword.dto.FaqKeywordResponseDto;
import com.bbteam.budgetbuddies.domain.faqkeyword.repository.FaqKeywordRepository;
import com.bbteam.budgetbuddies.domain.searchkeyword.domain.SearchKeyword;
import com.bbteam.budgetbuddies.domain.searchkeyword.repository.SearchKeywordRepository;
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
    private final FaqKeywordRepository faqKeywordRepository;
    private final SearchKeywordRepository searchKeywordRepository;

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

    @Override
    public Page<FaqResponseDto.FaqFindResponse> searchFaq(Pageable pageable, String searchCondition) {
        return faqRepository.searchFaq(pageable, searchCondition).map(FaqConverter::entityToFind);
    }

    @Override
    @Transactional
    public FaqKeywordResponseDto addKeyword(Long faqId, Long searchKeywordId) {
        Faq faq = findFaq(faqId);
        SearchKeyword searchKeyword = searchKeywordRepository.findById(searchKeywordId).orElseThrow(() -> new GeneralException(ErrorStatus._SEARCH_KEYWORD_NOT_FOUND));

        FaqKeyword faqKeyword = FaqKeyword.builder()
                .searchKeyword(searchKeyword)
                .faq(faq)
                .build();

        faqKeywordRepository.save(faqKeyword);
        return FaqKeywordResponseDto.toDto(faqKeyword);
    }

    @Override
    @Transactional
    public String removeKeyword(Long faqId, Long searchKeywordId) {
        Faq faq = findFaq(faqId);
        SearchKeyword searchKeyword = searchKeywordRepository.findById(searchKeywordId).orElseThrow(() -> new GeneralException(ErrorStatus._SEARCH_KEYWORD_NOT_FOUND));

        FaqKeyword faqKeyword = faqKeywordRepository.findByFaqAndSearchKeyword(faq, searchKeyword).orElseThrow(() -> new GeneralException(ErrorStatus._FAQ_KEYWORD_NOT_FOUND));
        faqKeywordRepository.delete(faqKeyword);

        return "ok";
    }
}
