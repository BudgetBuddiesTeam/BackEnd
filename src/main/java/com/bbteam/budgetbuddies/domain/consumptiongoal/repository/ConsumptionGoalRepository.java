package com.bbteam.budgetbuddies.domain.consumptiongoal.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AvgConsumptionGoalDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.CategoryConsumptionCountDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MyConsumptionGoalDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.enums.Gender;

@Repository
public interface ConsumptionGoalRepository extends JpaRepository<ConsumptionGoal, Long> {

	@Query(value = "SELECT cg FROM ConsumptionGoal AS cg WHERE cg.user.id = :userId AND cg.goalMonth = :goalMonth")
	List<ConsumptionGoal> findConsumptionGoalByUserIdAndGoalMonth(Long userId, LocalDate goalMonth);

	Optional<ConsumptionGoal> findConsumptionGoalByUserAndCategoryAndGoalMonth(User user, Category category,
		LocalDate goalMonth);

	@Query("SELECT AVG(cg.consumeAmount) FROM ConsumptionGoal cg " +
		"JOIN cg.category c " +
		"WHERE c.id = :categoryId " +
		"AND cg.deleted = false " +
		"AND cg.goalMonth BETWEEN :startOfWeek AND :endOfWeek " +
		"AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " +
		"AND cg.user.gender = :peerGender")
	Optional<Long> findAvgConsumptionByCategoryIdAndCurrentWeek(
		@Param("categoryId") Long categoryId,
		@Param("startOfWeek") LocalDate startOfWeek,
		@Param("endOfWeek") LocalDate endOfWeek,
		@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd,
		@Param("peerGender") Gender peerGender);

	@Query("SELECT new com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AvgConsumptionGoalDto(" +
		"cg.category.id, AVG(cg.consumeAmount))" +
		"FROM ConsumptionGoal cg " +
		"WHERE cg.category.isDefault = true " +
		"AND cg.deleted = false " +
		"AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " +
		"AND cg.user.gender = :peerGender " +
		"AND cg.goalMonth >= :currentMonth " +
		"GROUP BY cg.category.id " +
		"ORDER BY AVG(cg.consumeAmount) DESC")
	List<AvgConsumptionGoalDto> findAvgConsumptionAmountByCategory(
		@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd,
		@Param("peerGender") Gender peerGender,
		@Param("currentMonth") LocalDate currentMonth);

	@Query("SELECT new com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MyConsumptionGoalDto(" +
		"cg.category.id, SUM(cg.consumeAmount)) " +
		"FROM ConsumptionGoal cg " +
		"WHERE cg.category.isDefault = true " +
		"AND cg.deleted = false " +
		"AND cg.user.id = :userId " +
		"GROUP BY cg.category.id " +
		"ORDER BY cg.category.id")
	List<MyConsumptionGoalDto> findAllConsumptionAmountByUserId(@Param("userId") Long userId);

	@Query("SELECT new com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AvgConsumptionGoalDto(" +
		"cg.category.id, AVG(cg.goalAmount))" +
		"FROM ConsumptionGoal cg " +
		"WHERE cg.category.isDefault = true " +
		"AND cg.deleted = false " +
		"AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " +
		"AND cg.user.gender = :peerGender " +
		"AND cg.goalMonth >= :currentMonth " +
		"GROUP BY cg.category.id " +
		"ORDER BY AVG(cg.goalAmount) DESC")
	List<AvgConsumptionGoalDto> findAvgGoalAmountByCategory(
		@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd,
		@Param("peerGender") Gender peerGender,
		@Param("currentMonth") LocalDate currentMonth);

	@Query("SELECT new com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MyConsumptionGoalDto(" +
		"cg.category.id, SUM(cg.goalAmount)) " +
		"FROM ConsumptionGoal cg " +
		"WHERE cg.category.isDefault = true " +
		"AND cg.deleted = false " +
		"AND cg.user.id = :userId " +
		"GROUP BY cg.category.id " +
		"ORDER BY cg.category.id")
	List<MyConsumptionGoalDto> findAllGoalAmountByUserId(@Param("userId") Long userId);

	@Query("SELECT new com.bbteam.budgetbuddies.domain.consumptiongoal.dto.CategoryConsumptionCountDto(" +
		"cg.category.id, COUNT(cg)) " +
		"FROM ConsumptionGoal cg " +
		"WHERE cg.category.isDefault = true " +
		"AND cg.deleted = false " +
		"AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " +
		"AND cg.user.gender = :peerGender " +
		"AND cg.goalMonth >= :currentMonth " +
		"AND cg.consumeAmount > 0 " +
		"GROUP BY cg.category.id " +
		"ORDER BY COUNT(cg) DESC")
	List<CategoryConsumptionCountDto> findTopCategoriesByConsumptionCount(
		@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd,
		@Param("peerGender") Gender peerGender,
		@Param("currentMonth") LocalDate currentMonth);

	@Modifying
	@Query("UPDATE ConsumptionGoal cg SET cg.deleted = TRUE WHERE cg.category.id = :categoryId AND cg.user.id = :userId")
	void softDeleteByCategoryIdAndUserId(@Param("categoryId") Long categoryId, @Param("userId") Long userId);

	@Query("SELECT cg FROM ConsumptionGoal cg WHERE cg.category.id = :categoryId AND cg.user.id = :userId AND cg.deleted = FALSE")
	Optional<ConsumptionGoal> findByCategoryIdAndUserId(@Param("categoryId") Long categoryId,
		@Param("userId") Long userId);
}
