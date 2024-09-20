package com.bbteam.budgetbuddies.domain.hashtag.service;

import com.bbteam.budgetbuddies.domain.hashtag.converter.HashtagConverter;
import com.bbteam.budgetbuddies.domain.hashtag.dto.HashtagRequest;
import com.bbteam.budgetbuddies.domain.hashtag.dto.HashtagResponse;
import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import com.bbteam.budgetbuddies.domain.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    private final HashtagConverter hashtagConverter;

    @Transactional
    @Override
    public List<HashtagResponse> getAllHashtag() {
        /**
         * 모든 해시태그를 찾아 리스트로 리턴합니다.
          */

        List<Hashtag> hashtags = hashtagRepository.findAll();

        return hashtags.stream()
            .map(hashtagConverter::toDto)
            .toList();
    }

    @Transactional
    @Override
    public HashtagResponse registerHashtag(HashtagRequest.RegisterHashtagDto registerHashtagDto) {
        /**
         * 해시태그를 등록합니다.
         */

        Hashtag hashtag = hashtagConverter.toEntity(registerHashtagDto);

        hashtagRepository.save(hashtag);

        return hashtagConverter.toDto(hashtag);
    }

    @Transactional
    @Override
    public HashtagResponse updateHashtag(HashtagRequest.UpdateHashtagDto updateHashtagDto) {
        /**
         * 해시태그 이름을 수정합니다.
         */

        Hashtag hashtag = hashtagRepository.findById(updateHashtagDto.getId())
            .orElseThrow(() -> new IllegalArgumentException("Hashtag not found"));

        hashtag.update(updateHashtagDto);

        hashtagRepository.save(hashtag);

        return hashtagConverter.toDto(hashtag);
    }

    @Transactional
    @Override
    public String deleteHashtag(Long hashtagId) {
        /**
         * 해시태그를 삭제합니다.
         */

        Hashtag hashtag = hashtagRepository.findById(hashtagId)
            .orElseThrow(() -> new IllegalArgumentException("Hashtag not found"));

        hashtagRepository.deleteById(hashtagId);

        return "Success";
    }

    @Transactional
    @Override
    public HashtagResponse getHashtagById(Long hashtagId) {
        /**
         * 특정 해시태그를 하나 조회합니다.
         */

        Hashtag hashtag = hashtagRepository.findById(hashtagId)
            .orElseThrow(() -> new IllegalArgumentException("Hashtag not found"));
        
        return hashtagConverter.toDto(hashtag);
    }
}
