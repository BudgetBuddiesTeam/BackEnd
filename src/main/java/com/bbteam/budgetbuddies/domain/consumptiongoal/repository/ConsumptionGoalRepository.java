package com.bbteam.budgetbuddies.domain.consumptiongoal.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.enums.Gender;

@Repository
public interface ConsumptionGoalRepository extends JpaRepository<ConsumptionGoal, Long> {

	@Query("SELECT cg FROM ConsumptionGoal cg "
		+ "WHERE cg.category.isDefault = true "
		+ "AND cg.user.age BETWEEN :peerAgeStart AND :peerAgeEnd "
		+ "AND cg.user.gender = :peerGender "
		+ "ORDER BY cg.goalAmount DESC limit :top")
	List<ConsumptionGoal> findTopCategoriesAndGoalAmount(
		@Param("top") int top,
		@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd,
		@Param("peerGender") Gender peerGender);

	@Query(value = "SELECT cg FROM ConsumptionGoal AS cg WHERE cg.user.id = :userId AND cg.goalMonth = :goalMonth")
	List<ConsumptionGoal> findConsumptionGoalByUserIdAndGoalMonth(Long userId, LocalDate goalMonth);

	@Query("SELECT cg FROM ConsumptionGoal cg JOIN cg.category c WHERE c.id = :categoryId AND cg.goalMonth BETWEEN :startOfWeek AND :endOfWeek ORDER BY cg.consumeAmount DESC")
	Optional<ConsumptionGoal> findTopConsumptionByCategoryIdAndCurrentWeek(@Param("categoryId") Long categoryId,
		@Param("startOfWeek") LocalDate startOfWeek, @Param("endOfWeek") LocalDate endOfWeek);
}
