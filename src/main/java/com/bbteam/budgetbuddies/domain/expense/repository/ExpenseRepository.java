package com.bbteam.budgetbuddies.domain.expense.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.CategoryConsumptionCountDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.enums.Gender;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	@Query("SELECT e FROM Expense e WHERE e.user = :user AND e.expenseDate BETWEEN :startDate AND :endDate ORDER BY e.expenseDate DESC")
	List<Expense> findAllByUserIdForPeriod(@Param("user") User user, @Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);

	List<Expense> findByCategoryIdAndUserIdAndExpenseDateBetweenAndDeletedFalse(Long categoryId, Long userId,
		LocalDateTime startDate, LocalDateTime endDate);

	List<Expense> findByCategoryIdAndUserIdAndDeletedFalse(Long categoryId, Long userId);

	@Modifying
	@Query("UPDATE Expense e SET e.deleted = TRUE WHERE e.id = :expenseId")
	void softDeleteById(@Param("expenseId") Long expenseId);

	@Query("SELECT new com.bbteam.budgetbuddies.domain.consumptiongoal.dto.CategoryConsumptionCountDto(" +
		"e.category.id, COUNT(e)) " +
		"FROM Expense e " +
		"WHERE e.category.isDefault = true " +
		"AND e.deleted = false " +
		"AND e.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " +
		"AND e.user.gender = :peerGender " +
		"AND e.expenseDate >= :currentMonth " +
		"AND e.amount > 0 " +
		"GROUP BY e.category.id " +
		"ORDER BY COUNT(e) DESC")
	List<CategoryConsumptionCountDto> findTopCategoriesByConsumptionCount(
		@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd,
		@Param("peerGender") Gender peerGender,
		@Param("currentMonth") LocalDateTime currentMonth);
}
