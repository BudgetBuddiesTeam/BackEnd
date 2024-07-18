package com.bbteam.budgetbuddies.domain.discountinfolike.repository;

import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfolike.entity.DiscountInfoLike;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountInfoLikeRepository extends JpaRepository<DiscountInfoLike, Long> {

    Optional<DiscountInfoLike> findByUserAndDiscountInfo(User user, DiscountInfo discountInfo);

}
