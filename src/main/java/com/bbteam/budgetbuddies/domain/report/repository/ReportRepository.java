package com.bbteam.budgetbuddies.domain.report.repository;

import com.bbteam.budgetbuddies.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {


}
