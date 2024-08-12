package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.ConsumptionAnalysisConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.ConsumptionGoalConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.PeerInfoConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.TopCategoryConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.TopConsumptionConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopConsumptionResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
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

		checkPeerInfo(userId, peerAgeS, peerAgeE, peerG);

		List<ConsumptionGoal> topGoals = consumptionGoalRepository.findTopCategoriesAndGoalAmount(top, peerAgeStart,
			peerAgeEnd, peerGender);
		return topGoals.stream().map(TopCategoryConverter::fromEntity).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public PeerInfoResponseDTO getPeerInfo(Long userId, int peerAgeS, int peerAgeE, String peerG) {

		checkPeerInfo(userId, peerAgeS, peerAgeE, peerG);

		return PeerInfoConverter.fromEntity(peerAgeStart, peerAgeEnd, peerGender);
	}

	@Override
	@Transactional(readOnly = true)
	public ConsumptionAnalysisResponseDTO getTopCategoryAndConsumptionAmount(Long userId) {

		checkPeerInfo(userId, 0, 0, "none");

		ConsumptionGoal topConsumptionGoal = consumptionGoalRepository.findTopCategoriesAndGoalAmount(1, peerAgeStart,
			peerAgeEnd, peerGender).get(0);

		LocalDate today = LocalDate.now();
		LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		ConsumptionGoal currentWeekConsumptionAmount = consumptionGoalRepository.findTopConsumptionByCategoryIdAndCurrentWeek(
				topConsumptionGoal.getCategory().getId(), startOfWeek, endOfWeek)
			.orElseThrow(() -> new IllegalArgumentException(
				"카테고리 ID " + topConsumptionGoal.getCategory().getId() + "에 대한 현재 주 소비 데이터가 없습니다."));

		Long totalConsumptionAmountForCurrentWeek = currentWeekConsumptionAmount.getConsumeAmount();

		return ConsumptionAnalysisConverter.fromEntity(topConsumptionGoal, totalConsumptionAmountForCurrentWeek);
	}

	private User findUserById(Long userId) {
		Optional<User> user = userRepository.findById(userId);

		if (user.isEmpty()) {
			throw new NoSuchElementException("유저를 찾을 수 없습니다.");
		}

		return user.get();
	}

	private void checkPeerInfo(Long userId, int peerAgeS, int peerAgeE, String peerG) {

		User user = findUserById(userId);

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

		return consumptionGoalConverter.toConsumptionGoalResponseListDto(response, thisMonth);
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
			.orElseGet(() -> generateNewConsumptionGoal(user, category, goalMonth));
	}

	private ConsumptionGoal generateNewConsumptionGoal(User user, Category category, LocalDate goalMonth) {
		return ConsumptionGoal.builder()
			.goalMonth(goalMonth)
			.user(user)
			.category(category)
			.consumeAmount(0L)
			.goalAmount(0L)
			.build();
	}

	@Override
	@Transactional(readOnly = true)
	public ConsumptionGoalResponseListDto findUserConsumptionGoalList(Long userId, LocalDate date) {
		LocalDate goalMonth = date.withDayOfMonth(1);
		Map<Long, ConsumptionGoalResponseDto> goalMap = initializeGoalMap(userId);

		updateGoalMapWithPreviousMonth(userId, goalMonth, goalMap);
		updateGoalMapWithCurrentMonth(userId, goalMonth, goalMap);

		List<ConsumptionGoalResponseDto> consumptionGoalList = new ArrayList<>(goalMap.values());

		return consumptionGoalConverter.toConsumptionGoalResponseListDto(
			orderByRemainingBalanceDescending(consumptionGoalList), goalMonth);
	}

	private Map<Long, ConsumptionGoalResponseDto> initializeGoalMap(Long userId) {
		return categoryRepository.findUserCategoryByUserId(userId)
			.stream()
			.collect(Collectors.toMap(Category::getId, consumptionGoalConverter::toConsumptionGoalResponseDto));
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

	private List<ConsumptionGoalResponseDto> orderByRemainingBalanceDescending(
		List<ConsumptionGoalResponseDto> consumptionGoalList) {
		return consumptionGoalList.stream()
			.sorted(Comparator.comparingLong(ConsumptionGoalResponseDto::getRemainingBalance).reversed())
			.toList();
	}

	@Override
	@Transactional
	public void recalculateConsumptionAmount(Expense expense, ExpenseUpdateRequestDto request, User user) {
		restorePreviousGoalConsumptionAmount(expense, user);
		calculatePresentGoalConsumptionAmount(request, user);
	}

	private void restorePreviousGoalConsumptionAmount(Expense expense, User user) {
		ConsumptionGoal previousConsumptionGoal = consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(
				user, expense.getCategory(), expense.getExpenseDate().toLocalDate().withDayOfMonth(1))
			.orElseThrow(() -> new IllegalArgumentException("Not found consumptionGoal"));

		previousConsumptionGoal.restoreConsumeAmount(expense.getAmount());
		consumptionGoalRepository.save(previousConsumptionGoal);
	}

	private void calculatePresentGoalConsumptionAmount(ExpenseUpdateRequestDto request, User user) {
		Category categoryToReplace = categoryRepository.findById(request.getCategoryId())
			.orElseThrow(() -> new IllegalArgumentException("Not found category"));

		ConsumptionGoal consumptionGoal = consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(
				user, categoryToReplace, request.getExpenseDate().toLocalDate().withDayOfMonth(1))
			.orElseGet(() -> this.generateGoalByPreviousOrElseNew(user, categoryToReplace,
				request.getExpenseDate().toLocalDate().withDayOfMonth(1)));

		consumptionGoal.updateConsumeAmount(request.getAmount());
		consumptionGoalRepository.save(consumptionGoal);
	}

	private ConsumptionGoal generateGoalByPreviousOrElseNew(User user, Category category, LocalDate goalMonth) {
		LocalDate previousMonth = goalMonth.minusMonths(1);

		return consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, category, previousMonth)
			.map(this::generateGoalByPrevious)
			.orElseGet(() -> generateNewConsumptionGoal(user, category, goalMonth));
	}

	private ConsumptionGoal generateGoalByPrevious(ConsumptionGoal consumptionGoal) {
		return ConsumptionGoal.builder()
			.goalMonth(consumptionGoal.getGoalMonth().plusMonths(1))
			.user(consumptionGoal.getUser())
			.category(consumptionGoal.getCategory())
			.consumeAmount(0L)
			.goalAmount(consumptionGoal.getGoalAmount())
			.build();
	}

	@Override
	public void updateConsumeAmount(Long userId, Long categoryId, Long amount) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Not found user"));

		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new IllegalArgumentException("Not found Category"));

		LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
		ConsumptionGoal consumptionGoal = consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(
			user, category, thisMonth).orElseGet(() -> generateNewConsumptionGoal(user, category, thisMonth));

		consumptionGoal.updateConsumeAmount(amount);
		consumptionGoalRepository.save(consumptionGoal);
	}

	@Override
	public List<TopConsumptionResponseDTO> getTopConsumption(int top, Long userId, int peerAgeS, int peerAgeE,
		String peerG) {

		checkPeerInfo(userId, peerAgeS, peerAgeE, peerG);

		List<ConsumptionGoal> topConsumptions = consumptionGoalRepository.findTopConsumptionAndConsumeAmount(top,
			peerAgeStart, peerAgeEnd, peerGender);

		return topConsumptions.stream().map(TopConsumptionConverter::fromEntity).collect(Collectors.toList());
	}
}
