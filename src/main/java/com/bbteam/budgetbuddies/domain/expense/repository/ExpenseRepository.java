package com.bbteam.budgetbuddies.domain.expense.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	@Query("SELECT e FROM Expense e WHERE e.user = :user AND e.expenseDate BETWEEN :startDate AND :endDate ORDER BY e.expenseDate DESC")
	List<Expense> findAllByUserIdForPeriod(@Param("user") User user,
		@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
