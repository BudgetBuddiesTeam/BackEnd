package com.bbteam.budgetbuddies.domain.supportinfo.repository;

import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface SupportInfoRepository extends JpaRepository<SupportInfo, Long> {

    @Query("SELECT i FROM SupportInfo i WHERE (i.startDate <= :endDate AND i.endDate >= :startDate)")
    Page<SupportInfo> findByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

}
