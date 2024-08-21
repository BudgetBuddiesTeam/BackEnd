package com.bbteam.budgetbuddies.domain.discountinfo.repository;

import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DiscountInfoRepository extends JpaRepository<DiscountInfo, Long> {

    @Query("SELECT i FROM DiscountInfo i WHERE (i.startDate <= :endDate AND i.endDate >= :startDate)" +
    " ORDER BY i.likeCount DESC")
    Page<DiscountInfo> findByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT i FROM DiscountInfo i WHERE (i.startDate <= :endDate AND i.endDate >= :startDate)" +
            " AND i.isInCalendar = TRUE " +
            " ORDER BY i.likeCount DESC")
    List<DiscountInfo> findByMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT i FROM DiscountInfo i WHERE (i.startDate <= :endDate AND i.endDate >= :startDate)" +
            " AND i.isInCalendar = TRUE " +
            " ORDER BY i.likeCount DESC" +
            " LIMIT 2")
    List<DiscountInfo> findRecommendInfoByMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
