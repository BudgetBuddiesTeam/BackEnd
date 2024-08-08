package com.bbteam.budgetbuddies.domain.supportinfolike.repository;

import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfolike.entity.DiscountInfoLike;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfolike.entity.SupportInfoLike;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupportInfoLikeRepository extends JpaRepository<SupportInfoLike, Long> {

    Optional<SupportInfoLike> findByUserAndSupportInfo(User user, SupportInfo supportInfo);

}
