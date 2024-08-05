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
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.enums.Gender;

@Repository
public interface ConsumptionGoalRepository extends JpaRepository<ConsumptionGoal, Long> {

	@Query("SELECT cg FROM ConsumptionGoal cg " + "WHERE cg.category.isDefault = true "
		+ "AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " + "AND cg.user.gender = :peerGender "
		+ "ORDER BY cg.goalAmount DESC limit :top")
	List<ConsumptionGoal> findTopCategoriesAndGoalAmountLimit(@Param("top") int top,
		@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd, @Param("peerGender") Gender peerGender);

	@Query("SELECT cg FROM ConsumptionGoal cg " + "WHERE cg.category.isDefault = true "
		+ "AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd " + "AND cg.user.gender = :peerGender "
		+ "ORDER BY cg.goalAmount DESC")
	Page<ConsumptionGoal> findTopCategoriesAndGoalAmount(@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd, @Param("peerGender") Gender peerGender, Pageable pageable);

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
		+ "ORDER BY cg.consumeAmount DESC limit :top")
	List<ConsumptionGoal> findTopConsumptionAndConsumeAmount(@Param("top") int top,
		@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd, @Param("peerGender") Gender peerGender);

}