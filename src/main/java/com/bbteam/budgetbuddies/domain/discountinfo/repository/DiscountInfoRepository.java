package com.bbteam.budgetbuddies.domain.discountinfo.repository;

import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface DiscountInfoRepository extends JpaRepository<DiscountInfo, Long> {

    @Query("SELECT i FROM DiscountInfo i WHERE (i.startDate <= :endDate AND i.endDate >= :startDate)")
    Page<DiscountInfo> findByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

}
