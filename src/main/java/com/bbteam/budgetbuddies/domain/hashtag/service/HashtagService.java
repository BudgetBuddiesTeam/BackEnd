package com.bbteam.budgetbuddies.domain.hashtag.service;


import com.bbteam.budgetbuddies.domain.hashtag.dto.HashtagRequest;
import com.bbteam.budgetbuddies.domain.hashtag.dto.HashtagResponse;

import java.util.List;

public interface HashtagService {

    List<HashtagResponse> getAllHashtag();

    HashtagResponse registerHashtag(HashtagRequest.RegisterHashtagDto registerHashtagDto);

    HashtagResponse updateHashtag(HashtagRequest.UpdateHashtagDto updateHashtagDto);

    String deleteHashtag(Long hashtagId);

    HashtagResponse getHashtagById(Long hashtagId);

}
