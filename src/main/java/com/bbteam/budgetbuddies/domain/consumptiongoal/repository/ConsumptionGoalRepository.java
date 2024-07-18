package com.bbteam.budgetbuddies.domain.consumptiongoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;

@Repository
public interface ConsumptionGoalRepository extends JpaRepository<ConsumptionGoal, Long> {
}