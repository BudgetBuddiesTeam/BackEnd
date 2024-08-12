package com.bbteam.budgetbuddies.domain.consumptiongoal.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.CategoryAvgConsumptionDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.enums.Gender;

@Repository
public interface ConsumptionGoalRepository extends JpaRepository<ConsumptionGoal, Long> {

	@Query("SELECT cg FROM ConsumptionGoal cg " + "WHERE cg.category.isDefault = true "
		+ "AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " + "AND cg.user.gender = :peerGender "
		+ "AND cg.goalMonth >= :currentMonth " + "ORDER BY cg.goalAmount DESC limit :top")
	List<ConsumptionGoal> findTopCategoriesAndGoalAmountLimit(@Param("top") int top,
		@Param("peerAgeStart") int peerAgeStart, @Param("peerAgeEnd") int peerAgeEnd,
		@Param("peerGender") Gender peerGender, @Param("currentMonth") LocalDate currentMonth);

	@Query("SELECT cg FROM ConsumptionGoal cg " + "WHERE cg.category.isDefault = true "
		+ "AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " + "AND cg.user.gender = :peerGender "
		+ "AND cg.goalMonth >= :currentMonth " + "ORDER BY cg.goalAmount DESC")
	Page<ConsumptionGoal> findTopCategoriesAndGoalAmount(@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd, @Param("peerGender") Gender peerGender,
		@Param("currentMonth") LocalDate currentMonth, Pageable pageable);

	@Query(value = "SELECT cg FROM ConsumptionGoal AS cg WHERE cg.user.id = :userId AND cg.goalMonth = :goalMonth")
	List<ConsumptionGoal> findConsumptionGoalByUserIdAndGoalMonth(Long userId, LocalDate goalMonth);

	Optional<ConsumptionGoal> findConsumptionGoalByUserAndCategoryAndGoalMonth(User user, Category category,
		LocalDate goalMonth);

	@Query("SELECT cg FROM ConsumptionGoal cg JOIN cg.category c WHERE c.id = :categoryId AND cg.goalMonth "
		+ "BETWEEN :startOfWeek AND :endOfWeek ORDER BY cg.consumeAmount DESC limit 1")
	Optional<ConsumptionGoal> findTopConsumptionByCategoryIdAndCurrentWeek(@Param("categoryId") Long categoryId,
		@Param("startOfWeek") LocalDate startOfWeek, @Param("endOfWeek") LocalDate endOfWeek);

	@Query("SELECT cg FROM ConsumptionGoal cg " + "WHERE cg.category.isDefault = true "
		+ "AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " + "AND cg.user.gender = :peerGender "
		+ "AND cg.goalMonth >= :currentMonth " + "ORDER BY cg.consumeAmount DESC limit :top")
	List<ConsumptionGoal> findTopConsumptionAndConsumeAmountLimit(@Param("top") int top,
		@Param("peerAgeStart") int peerAgeStart, @Param("peerAgeEnd") int peerAgeEnd,
		@Param("peerGender") Gender peerGender, @Param("currentMonth") LocalDate currentMonth);

	@Query("SELECT cg FROM ConsumptionGoal cg " + "WHERE cg.category.isDefault = true "
		+ "AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " + "AND cg.user.gender = :peerGender "
		+ "AND cg.goalMonth >= :currentMonth " + "ORDER BY cg.consumeAmount DESC")
	Page<ConsumptionGoal> findTopConsumptionAndConsumeAmount(@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd, @Param("peerGender") Gender peerGender,
		@Param("currentMonth") LocalDate currentMonth, Pageable pageable);

	@Query(
		"SELECT new com.bbteam.budgetbuddies.domain.consumptiongoal.dto.CategoryAvgConsumptionDTO(cg.category.id, AVG(cg.consumeAmount))"
			+ "FROM ConsumptionGoal cg " + "WHERE cg.category.isDefault = true "
			+ "AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " + "AND cg.user.gender = :peerGender "
			+ "AND cg.goalMonth >= :currentMonth " + "GROUP BY cg.category.id " + "ORDER BY AVG(cg.consumeAmount) DESC")
	List<CategoryAvgConsumptionDTO> findAvgConsumptionByCategory(
		@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd,
		@Param("peerGender") Gender peerGender,
		@Param("currentMonth") LocalDate currentMonth);

}
