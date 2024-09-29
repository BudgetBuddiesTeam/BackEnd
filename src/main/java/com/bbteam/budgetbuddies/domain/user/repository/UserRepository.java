package com.bbteam.budgetbuddies.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbteam.budgetbuddies.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findById(Long userId);

	Optional<User> findFirstByPhoneNumber(String phoneNumber);
}
