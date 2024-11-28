package com.bbteam.budgetbuddies.domain.searchkeyword.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.searchkeyword.domain.SearchKeyword;
import com.bbteam.budgetbuddies.domain.searchkeyword.service.SearchKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search-keyword")
public class SearchKeywordController {

    private final SearchKeywordService searchKeywordService;

    @PostMapping("")
    public ApiResponse<SearchKeyword> saveKeyword(String keyword) {

        return ApiResponse.onSuccess(searchKeywordService.saveKeyword(keyword));
    }

    @GetMapping("")
    public ApiResponse<SearchKeyword> findOne(Long searchKeywordId) {
        return ApiResponse.onSuccess(searchKeywordService.findOne(searchKeywordId));
    }

    @GetMapping("/all")
    public ApiResponse<Page<SearchKeyword>> findAll(Pageable pageable) {
        return ApiResponse.onSuccess(searchKeywordService.findAll(pageable));
    }

    @PutMapping("")
    public ApiResponse<SearchKeyword> modifyOne(Long searchKeywordId, String newKeyword) {
        return ApiResponse.onSuccess((searchKeywordService.modifyOne(searchKeywordId, newKeyword)));
    }

    @DeleteMapping("")
    public ApiResponse<String> deleteOne(Long searchKeywordId) {
        searchKeywordService.deleteOne(searchKeywordId);
        return ApiResponse.onSuccess("OK");
    }




}
