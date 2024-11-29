package com.bbteam.budgetbuddies.domain.hashtag.converter;

import com.bbteam.budgetbuddies.domain.hashtag.dto.HashtagRequest;
import com.bbteam.budgetbuddies.domain.hashtag.dto.HashtagResponse;
import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import org.springframework.stereotype.Service;

@Service
public class HashtagConverter {

    /**
     * @param entity
     * @return responseDto
     */

    public HashtagResponse toDto(Hashtag hashtag) {

        return HashtagResponse.builder()
            .id(hashtag.getId())
            .name(hashtag.getName())
            .build();

    }

    /**
     * @param requestDto
     * @return entity
     */
    public Hashtag toEntity(HashtagRequest.RegisterHashtagDto registerHashtagDto) {

        return Hashtag.builder()
            .name(registerHashtagDto.getName())
            .build();

    }

}
