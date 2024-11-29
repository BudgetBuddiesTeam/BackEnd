package com.bbteam.budgetbuddies.domain.connectedinfo.repository;

import com.bbteam.budgetbuddies.domain.connectedinfo.entity.ConnectedInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectedInfoRepository extends JpaRepository<ConnectedInfo, Long> {

    List<ConnectedInfo> findAllByDiscountInfo(DiscountInfo discountInfo);

    void deleteAllByDiscountInfo(DiscountInfo discountInfo);

    List<ConnectedInfo> findAllBySupportInfo(SupportInfo supportInfo);

    void deleteAllBySupportInfo(SupportInfo supportInfo);
}
