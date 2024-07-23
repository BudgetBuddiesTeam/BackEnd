package com.bbteam.budgetbuddies.domain.supportinfo.repository;

import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SupportInfoRepository extends JpaRepository<SupportInfo, Long> {

    @Query("SELECT i FROM SupportInfo i WHERE (i.startDate <= :endDate AND i.endDate >= :startDate)")
    Page<SupportInfo> findByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT i FROM SupportInfo i WHERE ((i.startDate BETWEEN :startDate AND :endDate) OR i.endDate BETWEEN :startDate AND :endDate)")
    List<SupportInfo> findByMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT i FROM SupportInfo i WHERE ((i.startDate BETWEEN :startDate AND :endDate) OR i.endDate BETWEEN :startDate AND :endDate)" +
            " ORDER BY i.likeCount DESC" +
            " LIMIT 2")
    List<SupportInfo> findRecommendInfoByMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
