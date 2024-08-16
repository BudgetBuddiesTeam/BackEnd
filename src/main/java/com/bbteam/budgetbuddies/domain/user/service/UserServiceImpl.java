package com.bbteam.budgetbuddies.domain.user.service;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.ConsumptionGoalConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.UserConsumptionGoalResponse;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;
import com.bbteam.budgetbuddies.domain.user.converter.UserConverter;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ConsumptionGoalRepository consumptionGoalRepository;
    private final ConsumptionGoalConverter consumptionGoalConverter;

    @Override
    @Transactional
    public List<UserConsumptionGoalResponse> createConsumptionGoalWithDefaultGoals(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Category> defaultCategories = categoryRepository.findAllByIsDefaultTrue();
        List<ConsumptionGoal> consumptionGoals = defaultCategories.stream()
                .map(category -> ConsumptionGoal.builder()
                        .user(user)
                        .category(category)
                        .goalMonth(LocalDate.now().withDayOfMonth(1))
                        .consumeAmount(0L)
                        .goalAmount(0L)
                        .build())
                .collect(Collectors.toList());

        List<ConsumptionGoal> savedConsumptionGoals = consumptionGoalRepository.saveAll(consumptionGoals);

        return savedConsumptionGoals.stream()
                .map(consumptionGoalConverter::toUserConsumptionGoalResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto.ResponseUserDto saveUser(UserDto.RegisterUserDto dto) {
        User user = UserConverter.toUser(dto);
        userRepository.save(user);
        return UserConverter.toDto(user);
    }

    @Override
    public UserDto.ResponseUserDto findUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당 유저는 존재하지 않습니다."));
        return UserConverter.toDto(user);
    }

    @Override
    @Transactional
    public UserDto.ResponseUserDto changeUser(Long userId, String email, String name) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No such user"));
        user.changeUserDate(email, name);
        return UserConverter.toDto(user);
    }

    @Override
    public List<UserDto.ResponseUserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserConverter::toDto)
                .toList();
    }
}
