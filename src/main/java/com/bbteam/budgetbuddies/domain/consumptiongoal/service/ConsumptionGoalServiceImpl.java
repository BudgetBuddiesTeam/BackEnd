package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.ConsumptionGoalConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.PeerInfoConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.TopCategoryConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import com.bbteam.budgetbuddies.enums.Gender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsumptionGoalServiceImpl implements ConsumptionGoalService {

	private final ConsumptionGoalRepository consumptionGoalRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;

	private final ConsumptionGoalConverter consumptionGoalConverter;

	private int peerAgeStart;
	private int peerAgeEnd;
	private Gender peerGender;

	@Override
	@Transactional(readOnly = true)
	public List<TopGoalCategoryResponseDTO> getTopGoalCategories(int top, Long userId, int peerAgeS, int peerAgeE,
		String peerG) {

		User user = findUserById(userId);

		checkPeerInfo(user, peerAgeS, peerAgeE, peerG);

		List<ConsumptionGoal> topGoals = consumptionGoalRepository.findTopCategoriesAndGoalAmount(top, peerAgeStart,
			peerAgeEnd, peerGender);
		return topGoals.stream().map(TopCategoryConverter::fromEntity).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public PeerInfoResponseDTO getPeerInfo(Long userId, int peerAgeS, int peerAgeE, String peerG) {

		User user = findUserById(userId);

		checkPeerInfo(user, peerAgeS, peerAgeE, peerG);

		return PeerInfoConverter.fromEntity(peerAgeStart, peerAgeEnd, peerGender);
	}

	private User findUserById(Long userId) {
		Optional<User> user = userRepository.findById(userId);

		if (user.isEmpty()) {
			throw new NoSuchElementException("유저를 찾을 수 없습니다.");
		}

		return user.get();
	}

	private void checkPeerInfo(User user, int peerAgeS, int peerAgeE, String peerG) {

		Gender gender = Gender.valueOf(peerG.toUpperCase());

		if (peerAgeS == 0 || peerAgeE == 0 || gender == Gender.NONE) {
			peerGender = user.getGender();
			setAgeGroupByUser(user.getAge());
		} else {
			peerAgeStart = peerAgeS;
			peerAgeEnd = peerAgeE;
			peerGender = gender;
		}
	}

	private void setAgeGroupByUser(int userAge) {
		if (userAge >= 20 && userAge <= 22) {
			peerAgeStart = 20;
			peerAgeEnd = 22;
		} else if (userAge >= 23 && userAge <= 25) {
			peerAgeStart = 23;
			peerAgeEnd = 25;
		} else if (userAge >= 26 && userAge <= 28) {
			peerAgeStart = 26;
			peerAgeEnd = 28;
		} else if (userAge >= 29) {
			peerAgeStart = 29;
			peerAgeEnd = 99;
		} else {
			peerAgeStart = 0;
			peerAgeEnd = 19;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ConsumptionGoalResponseListDto findUserConsumptionGoal(Long userId, LocalDate date) {
		LocalDate goalMonth = date.withDayOfMonth(1);
		Map<Long, ConsumptionGoalResponseDto> goalMap = initializeGoalMap(userId, goalMonth);

		updateGoalMapWithPreviousMonth(userId, goalMonth, goalMap);
		updateGoalMapWithCurrentMonth(userId, goalMonth, goalMap);

		return new ConsumptionGoalResponseListDto(goalMonth, new ArrayList<>(goalMap.values()));
	}

	private Map<Long, ConsumptionGoalResponseDto> initializeGoalMap(Long userId, LocalDate goalMonth) {
		return categoryRepository.findUserCategoryByUserId(userId)
			.stream()
			.collect(Collectors.toMap(Category::getId,
				category -> consumptionGoalConverter.toConsumptionGoalResponseDto(category)));
	}

	private void updateGoalMapWithPreviousMonth(Long userId, LocalDate goalMonth,
		Map<Long, ConsumptionGoalResponseDto> goalMap) {
		updateGoalMap(userId, goalMonth.minusMonths(1), goalMap);
	}

	private void updateGoalMapWithCurrentMonth(Long userId, LocalDate goalMonth,
		Map<Long, ConsumptionGoalResponseDto> goalMap) {
		updateGoalMap(userId, goalMonth, goalMap);
	}

	private void updateGoalMap(Long userId, LocalDate month, Map<Long, ConsumptionGoalResponseDto> goalMap) {
		consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(userId, month)
			.stream()
			.map(consumptionGoalConverter::toConsumptionGoalResponseDto)
			.forEach(goal -> goalMap.put(goal.getCategoryId(), goal));
	}

	@Override
	@Transactional
	public ConsumptionGoalResponseListDto updateConsumptionGoals(Long userId,
		ConsumptionGoalListRequestDto consumptionGoalListRequestDto) {
		LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Not found user"));

		List<ConsumptionGoal> updatedConsumptionGoal = consumptionGoalListRequestDto.getConsumptionGoalList()
			.stream()
			.map(c -> updateConsumptionGoalWithRequestDto(user, c, thisMonth))
			.toList();

		List<ConsumptionGoalResponseDto> response = consumptionGoalRepository.saveAll(updatedConsumptionGoal)
			.stream()
			.map(consumptionGoalConverter::toConsumptionGoalResponseDto)
			.toList();

		return new ConsumptionGoalResponseListDto(thisMonth, response);
	}

	private ConsumptionGoal updateConsumptionGoalWithRequestDto(User user,
		ConsumptionGoalRequestDto consumptionGoalRequestDto, LocalDate goalMonth) {

		Category category = categoryRepository.findById(consumptionGoalRequestDto.getCategoryId())
			.orElseThrow(() -> new IllegalArgumentException("Not found Category"));

		ConsumptionGoal consumptionGoal = findOrElseGenerateConsumptionGoal(user, category, goalMonth);
		consumptionGoal.updateGoalAmount(consumptionGoalRequestDto.getGoalAmount());

		return consumptionGoal;
	}

	private ConsumptionGoal findOrElseGenerateConsumptionGoal(User user, Category category, LocalDate goalMonth) {
		return consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, category, goalMonth)
			.orElseGet(() -> generateConsumptionGoal(user, category, goalMonth));
	}

	private ConsumptionGoal generateConsumptionGoal(User user, Category category, LocalDate goalMonth) {
		return ConsumptionGoal.builder().goalMonth(goalMonth).user(user).category(category).consumeAmount(0L).build();
	}
}