package com.bbteam.budgetbuddies.domain.favoritehashtag.repository;

import com.bbteam.budgetbuddies.domain.favoritehashtag.entity.FavoriteHashtag;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteHashtagRepository extends JpaRepository<FavoriteHashtag, Long> {
    List<FavoriteHashtag> findByUser(User user);

    List<FavoriteHashtag> findByHashtagIdIn(List<Long> hashtagIds);
}