package com.bbteam.budgetbuddies.domain.hashtag.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.hashtag.dto.HashtagRequest;
import com.bbteam.budgetbuddies.domain.hashtag.dto.HashtagResponse;
import com.bbteam.budgetbuddies.domain.hashtag.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hashtags")
public class HashtagController implements HashtagApi {

    private final HashtagService hashtagService;

    @Override
    @GetMapping("/find-all")
    public ApiResponse<List<HashtagResponse>> getAllHashtag() {

        List<HashtagResponse> hashtagResponses = hashtagService.getAllHashtag();

        return ApiResponse.onSuccess(hashtagResponses);
    }

    @Override
    @PostMapping("")
    public ApiResponse<HashtagResponse> registerHashtag(
        @RequestBody HashtagRequest.RegisterHashtagDto registerHashtagDto
    ) {

        HashtagResponse hashtagResponse = hashtagService.registerHashtag(registerHashtagDto);

        return ApiResponse.onSuccess(hashtagResponse);
    }

    @Override
    @PutMapping("")
    public ApiResponse<HashtagResponse> updateHashtag(
        @RequestBody HashtagRequest.UpdateHashtagDto updateHashtagDto
    ) {

        HashtagResponse hashtagResponse = hashtagService.updateHashtag(updateHashtagDto);

        return ApiResponse.onSuccess(hashtagResponse);
    }

    @Override
    @DeleteMapping("/{hashtagId}")
    public ApiResponse<String> deleteHashtag(
        @PathVariable Long hashtagId
    ) {
        String message = hashtagService.deleteHashtag(hashtagId);

        return ApiResponse.onSuccess(message);
    }

    @Override
    @GetMapping("/{hashtagId}")
    public ApiResponse<HashtagResponse> getHashtag(
        @PathVariable Long hashtagId
    ) {
        HashtagResponse hashtagResponse = hashtagService.getHashtagById(hashtagId);

        return ApiResponse.onSuccess(hashtagResponse);
    }
}
