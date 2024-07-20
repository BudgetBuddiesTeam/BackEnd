package com.bbteam.budgetbuddies.domain.consumptiongoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;

@Repository
public interface ConsumptionGoalRepository extends JpaRepository<ConsumptionGoal, Long> {

	@Query("SELECT cg FROM ConsumptionGoal cg " +
		"WHERE cg.category.isDefault = true " +
		"ORDER BY cg.goalAmount DESC")
	List<ConsumptionGoal> findTopCategoriesAndGoalAmount();
}