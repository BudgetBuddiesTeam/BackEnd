package com.bbteam.budgetbuddies.domain.consumptiongoal.repository;

import java.util.List;

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
		+ "ORDER BY cg.goalAmount DESC")
	List<ConsumptionGoal> findTopCategoriesAndGoalAmount(
		@Param("peerAgeStart") int peerAgeStart,
		@Param("peerAgeEnd") int peerAgeEnd,
		@Param("peerGender") Gender peerGender);
}