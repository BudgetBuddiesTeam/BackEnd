package com.bbteam.budgetbuddies.domain.supportinfolike.repository;

import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfolike.entity.DiscountInfoLike;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfolike.entity.SupportInfoLike;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupportInfoLikeRepository extends JpaRepository<SupportInfoLike, Long> {

    Optional<SupportInfoLike> findByUserAndSupportInfo(User user, SupportInfo supportInfo);

    // 사용자가 좋아요를 누른 시점을 기준으로 최신순 정렬
    Page<SupportInfoLike> findAllByUserOrderByUpdatedAtDesc(User user, Pageable pageable);

}
