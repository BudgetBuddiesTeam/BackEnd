package com.bbteam.budgetbuddies.domain.searchkeyword.service;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.searchkeyword.domain.SearchKeyword;
import com.bbteam.budgetbuddies.domain.searchkeyword.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchKeywordService {

    private final SearchKeywordRepository searchKeywordRepository;

    public SearchKeyword saveKeyword(String keyword) {
        SearchKeyword searchKeyword = SearchKeyword.builder().keyword(keyword).build();
        searchKeywordRepository.save(searchKeyword);
        return searchKeyword;
    }

    public SearchKeyword findOne(Long searchKeywordId) {
        return searchKeywordRepository.findById(searchKeywordId).orElseThrow(() -> new GeneralException(ErrorStatus._SEARCH_KEYWORD_NOT_FOUND));
    }

    public Page<SearchKeyword> findAll(Pageable pageable) {
        return searchKeywordRepository.findAll(pageable);
    }

    public SearchKeyword modifyOne(Long searchKeywordId, String keyword) {
        SearchKeyword searchKeyword = searchKeywordRepository.findById(searchKeywordId).orElseThrow(() -> new GeneralException(ErrorStatus._SEARCH_KEYWORD_NOT_FOUND));
        searchKeyword.changeKeyword(keyword);
        return searchKeyword;
    }

    public void deleteOne(Long searchKeywordId) {
        SearchKeyword searchKeyword = searchKeywordRepository.findById(searchKeywordId).orElseThrow(() -> new GeneralException(ErrorStatus._SEARCH_KEYWORD_NOT_FOUND));
        searchKeywordRepository.delete(searchKeyword);
    }
}
