package com.bbteam.budgetbuddies.domain.hashtag.repository;

import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    // 해시태그 아이디를 기반으로 해시태그 엔티티 가져오기
    List<Hashtag> findByIdIn(List<Long> hashtagIds);

}
