package com.bbteam.budgetbuddies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableCaching
public class BudgetbuddiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetbuddiesApplication.class, args);
	}

}
