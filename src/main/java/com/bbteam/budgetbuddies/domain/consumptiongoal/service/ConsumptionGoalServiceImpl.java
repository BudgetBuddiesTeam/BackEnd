package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.ConsumptionGoalConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AllConsumptionCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AvgConsumptionGoalDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.CategoryConsumptionCountDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumeAmountAndGoalAmountDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MonthReportResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MyConsumptionGoalDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopCategoryConsumptionDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.expense.repository.ExpenseRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import com.bbteam.budgetbuddies.enums.Gender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumptionGoalServiceImpl implements ConsumptionGoalService {

	private final ConsumptionGoalRepository consumptionGoalRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;

	private final ConsumptionGoalConverter consumptionGoalConverter;
	private final LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
	private final LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	private final LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
	private final ExpenseRepository expenseRepository;
	private int peerAgeStart;
	private int peerAgeEnd;
	private Gender peerGender;

	@Override
	@Transactional(readOnly = true)
	public List<TopGoalCategoryResponseDto> getTopConsumptionGoalCategories(Long userId, int peerAgeS, int peerAgeE,
		String peerG) {

		checkPeerInfo(userId, peerAgeS, peerAgeE, peerG);

		List<AvgConsumptionGoalDto> categoryAvgList = getAvgGoalAmount();

		return categoryAvgList.stream()
			.sorted(Comparator.comparing(AvgConsumptionGoalDto::getAverageAmount).reversed())
			.limit(4)
			.map(avgGoal -> TopGoalCategoryResponseDto.builder()
				.categoryName(getCategoryNameById(avgGoal.getCategoryId()))
				.goalAmount(avgGoal.getAverageAmount())
				.build())
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<AllConsumptionCategoryResponseDto> getAllConsumptionGoalCategories(Long userId, int peerAgeS,
		int peerAgeE, String peerG) {

		checkPeerInfo(userId, peerAgeS, peerAgeE, peerG);

		List<AvgConsumptionGoalDto> categoryAvgList = getMedianGoalAmount();

		List<MyConsumptionGoalDto> myConsumptionAmountList = getMyGoalAmount(userId);

		List<Category> defaultCategories = categoryRepository.findAllByIsDefaultTrue();
		return defaultCategories.stream().map(category -> {
			MyConsumptionGoalDto myConsumptionAmountDto = myConsumptionAmountList.stream()
				.filter(dto -> dto.getCategoryId().equals(category.getId()))
				.findFirst()
				.orElse(new MyConsumptionGoalDto(category.getId(), 0L));

			AvgConsumptionGoalDto avgDto = categoryAvgList.stream()
				.filter(dto -> dto.getCategoryId().equals(category.getId()))
				.findFirst()
				.orElse(new AvgConsumptionGoalDto(category.getId(), 0L));

			Long avgConsumeAmount = avgDto.getAverageAmount();
			Long myConsumeAmount = myConsumptionAmountDto.getMyAmount();
			Long roundedAvgConsumeAmount = roundToNearest10(avgConsumeAmount);

			long consumeAmountDifference;

			if (roundedAvgConsumeAmount == 0L) {
				consumeAmountDifference = -myConsumeAmount;
			} else {
				consumeAmountDifference = myConsumeAmount - roundedAvgConsumeAmount;
			}

			return AllConsumptionCategoryResponseDto.builder()
				.categoryName(category.getName())
				.avgAmount(roundedAvgConsumeAmount)
				.amountDifference(consumeAmountDifference)
				.build();
		}).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public PeerInfoResponseDto getPeerInfo(Long userId, int peerAgeS, int peerAgeE, String peerG) {

		checkPeerInfo(userId, peerAgeS, peerAgeE, peerG);

		return consumptionGoalConverter.toPeerInfo(peerAgeStart, peerAgeEnd, peerGender);
	}

	@Override
	@Transactional(readOnly = true)
	public ConsumptionAnalysisResponseDto getTopCategoryAndConsumptionAmount(Long userId) {

		checkPeerInfo(userId, 0, 0, "none");

		List<AvgConsumptionGoalDto> avgConsumptionGoalList = consumptionGoalRepository.findAvgGoalAmountByCategory(
			peerAgeStart, peerAgeEnd, peerGender, currentMonth);

		Long topConsumptionGoalCategoryId = avgConsumptionGoalList.get(0).getCategoryId();

		LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
		LocalDateTime endOfWeekDateTime = endOfWeek.atTime(LocalTime.MAX);

		Long currentWeekConsumptionAmount = consumptionGoalRepository.findAvgConsumptionByCategoryIdAndCurrentWeek(
				topConsumptionGoalCategoryId, startOfWeekDateTime, endOfWeekDateTime, peerAgeStart, peerAgeEnd, peerGender)
			.orElse(0L);

		currentWeekConsumptionAmount = roundToNearest10(currentWeekConsumptionAmount);

		String topGoalCategory = getCategoryNameById(topConsumptionGoalCategoryId);

		return consumptionGoalConverter.toTopCategoryAndConsumptionAmount(topGoalCategory,
			currentWeekConsumptionAmount);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TopCategoryConsumptionDto> getTopConsumptionCategories(Long userId, int peerAgeS, int peerAgeE,
		String peerG) {

		checkPeerInfo(userId, peerAgeS, peerAgeE, peerG);

		List<CategoryConsumptionCountDto> categoryConsumptionCountDto = expenseRepository.findTopCategoriesByConsumptionCount(
			peerAgeStart, peerAgeEnd, peerGender, currentMonth.atStartOfDay());

		return categoryConsumptionCountDto.stream()
			.limit(3)
			.map(category -> TopCategoryConsumptionDto.builder()
				.categoryName(getCategoryNameById(category.getCategoryId()))
				.consumptionCount(category.getConsumptionCount())
				.build())
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<AllConsumptionCategoryResponseDto> getAllConsumptionCategories(Long userId, int peerAgeS, int peerAgeE,
		String peerG) {

		checkPeerInfo(userId, peerAgeS, peerAgeE, peerG);

		List<AvgConsumptionGoalDto> categoryAvgList = getMedianConsumeAmount();

		List<MyConsumptionGoalDto> myConsumptionAmountList = getMyConsumptionAmount(userId);

		List<Category> defaultCategories = categoryRepository.findAllByIsDefaultTrue();

		return defaultCategories.stream().map(category -> {
			MyConsumptionGoalDto myConsumptionAmountDto = myConsumptionAmountList.stream()
				.filter(dto -> dto.getCategoryId().equals(category.getId()))
				.findFirst()
				.orElse(new MyConsumptionGoalDto(category.getId(), 0L));

			AvgConsumptionGoalDto avgDto = categoryAvgList.stream()
				.filter(dto -> dto.getCategoryId().equals(category.getId()))
				.findFirst()
				.orElse(new AvgConsumptionGoalDto(category.getId(), 0L));

			Long avgConsumeAmount = avgDto.getAverageAmount();
			Long myConsumeAmount = myConsumptionAmountDto.getMyAmount();
			Long roundedAvgConsumeAmount = roundToNearest10(avgConsumeAmount);

			long consumeAmountDifference;
			if (roundedAvgConsumeAmount == 0L) {
				consumeAmountDifference = -myConsumeAmount;
			} else {
				consumeAmountDifference = myConsumeAmount - roundedAvgConsumeAmount;
			}

			return AllConsumptionCategoryResponseDto.builder()
				.categoryName(category.getName())
				.avgAmount(roundedAvgConsumeAmount)
				.amountDifference(consumeAmountDifference)
				.build();
		}).toList();
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
			peerAgeStart = 1;
			peerAgeEnd = 19;
		}
	}
	// 평균소비목표금액을 가져오는 메서드
	// private List<AvgConsumptionGoalDto> getAvgConsumptionAmount() {
	//
	// 	List<Category> defaultCategories = categoryRepository.findAllByIsDefaultTrue();
	// 	List<AvgConsumptionGoalDto> categoryAvgList = new ArrayList<>();
	//
	// 	List<AvgConsumptionGoalDto> avgConsumptionGoalDto = consumptionGoalRepository
	// 		.findAvgConsumptionAmountByCategory(peerAgeStart, peerAgeEnd, peerGender, currentMonth);
	//
	// 	Map<Long, AvgConsumptionGoalDto> categoryAvgMap = avgConsumptionGoalDto.stream()
	// 		.collect(Collectors.toMap(AvgConsumptionGoalDto::getCategoryId, Function.identity()));
	//
	// 	for (Category category : defaultCategories) {
	// 		AvgConsumptionGoalDto avgDto = categoryAvgMap.getOrDefault(category.getId(),
	// 			new AvgConsumptionGoalDto(category.getId(), 0.0));
	//
	// 		categoryAvgList.add(avgDto);
	// 	}
	// 	return categoryAvgList;
	// }

	private List<MyConsumptionGoalDto> getMyConsumptionAmount(Long userId) {

		List<Category> defaultCategories = categoryRepository.findAllByIsDefaultTrue();
		List<MyConsumptionGoalDto> myConsumptionAmountList = new ArrayList<>();

		List<MyConsumptionGoalDto> myConsumptionGoalDto = consumptionGoalRepository.findAllConsumptionAmountByUserId(
			userId, currentMonth);

		Map<Long, MyConsumptionGoalDto> myConsumptionMap = myConsumptionGoalDto.stream()
			.collect(Collectors.toMap(MyConsumptionGoalDto::getCategoryId, Function.identity()));

		for (Category category : defaultCategories) {
			MyConsumptionGoalDto myConsumptionGaolDto = myConsumptionMap.getOrDefault(category.getId(),
				new MyConsumptionGoalDto(category.getId(), 0L));

			myConsumptionAmountList.add(myConsumptionGaolDto);
		}
		return myConsumptionAmountList;
	}

	private List<AvgConsumptionGoalDto> getAvgGoalAmount() {

		List<Category> defaultCategories = categoryRepository.findAllByIsDefaultTrue();
		List<AvgConsumptionGoalDto> categoryAvgList = new ArrayList<>();

		List<AvgConsumptionGoalDto> avgConsumptionGoalDto = consumptionGoalRepository.findAvgGoalAmountByCategory(
			peerAgeStart, peerAgeEnd, peerGender, currentMonth);

		Map<Long, AvgConsumptionGoalDto> categoryAvgMap = avgConsumptionGoalDto.stream()
			.collect(Collectors.toMap(AvgConsumptionGoalDto::getCategoryId, Function.identity()));

		for (Category category : defaultCategories) {
			AvgConsumptionGoalDto avgDto = categoryAvgMap.getOrDefault(category.getId(),
				new AvgConsumptionGoalDto(category.getId(), 0.0));

			categoryAvgList.add(avgDto);
		}
		return categoryAvgList;
	}

	private List<MyConsumptionGoalDto> getMyGoalAmount(Long userId) {

		List<Category> defaultCategories = categoryRepository.findAllByIsDefaultTrue();
		List<MyConsumptionGoalDto> myConsumptionAmountList = new ArrayList<>();

		List<MyConsumptionGoalDto> myConsumptionGoalDto = consumptionGoalRepository.findAllGoalAmountByUserId(userId,
			currentMonth);

		Map<Long, MyConsumptionGoalDto> myConsumptionMap = myConsumptionGoalDto.stream()
			.collect(Collectors.toMap(MyConsumptionGoalDto::getCategoryId, Function.identity()));

		for (Category category : defaultCategories) {
			MyConsumptionGoalDto myDto = myConsumptionMap.getOrDefault(category.getId(),
				new MyConsumptionGoalDto(category.getId(), 0L));

			myConsumptionAmountList.add(myDto);
		}
		return myConsumptionAmountList;
	}

	private String getCategoryNameById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new RuntimeException("카테고리 " + categoryId + "를 찾을 수 없습니다.: "));
		return category.getName();
	}

	private Long roundToNearest10(Long amount) {
		if (amount == null) {
			return 0L;
		}
		BigDecimal decimalAmount = BigDecimal.valueOf(amount);
		BigDecimal roundedAmount = decimalAmount.divide(BigDecimal.valueOf(10), RoundingMode.HALF_UP)
			.multiply(BigDecimal.valueOf(10));
		return roundedAmount.longValue();
	}

	private List<AvgConsumptionGoalDto> getMedianGoalAmount() {

		/**
		 * 기본 카테고리만 가져와서 리스트에 저장
		 * 기본 카테고리 id 별로 소비 목표 데이터를 가져와 리스트로 저장
		 * 데이터가 존재하는 경우 리스트의 중앙값 계산
		 * 리스트가 비어 있으면 기본 값 0으로 설정
		 * 카테고리 별 중앙값 리스트 반환
		 */

		List<Category> defaultCategories = categoryRepository.findAllByIsDefaultTrue();

		List<AvgConsumptionGoalDto> categoryMedianList = new ArrayList<>();

		for (Category category : defaultCategories) {

			List<Double> goalAmounts = consumptionGoalRepository.findGoalAmountsByCategories(peerAgeStart, peerAgeEnd,
				peerGender, currentMonth, category.getId());

			if (goalAmounts != null && !goalAmounts.isEmpty()) {

				double median = calculateMedian(goalAmounts);
				categoryMedianList.add(new AvgConsumptionGoalDto(category.getId(), median));
			} else {
				// 데이터가 없는 경우 기본 값으로
				categoryMedianList.add(new AvgConsumptionGoalDto(category.getId(), 0.0));
			}
		}
		return categoryMedianList;
	}

	private List<AvgConsumptionGoalDto> getMedianConsumeAmount() {

		/*
		 * 기본 카테고리만 가져와서 리스트에 저장
		 * 기본 카테고리 id 별로 소비 금액 데이터를 가져와 리스트로 저장
		 * 데이터가 존재하는 경우 리스트의 중앙값 계산
		 * 리스트가 비어 있으면 기본 값 0으로 설정
		 * 카테고리 별 중앙값 리스트 반환
		 */

		List<Category> defaultCategories = categoryRepository.findAllByIsDefaultTrue();

		List<AvgConsumptionGoalDto> categoryMedianList = new ArrayList<>();

		for (Category category : defaultCategories) {

			List<Double> goalAmounts = consumptionGoalRepository.findConsumeAmountsByCategories(peerAgeStart,
				peerAgeEnd, peerGender, currentMonth, category.getId());

			if (goalAmounts != null && !goalAmounts.isEmpty()) {
				double median = calculateMedian(goalAmounts);
				categoryMedianList.add(new AvgConsumptionGoalDto(category.getId(), median));
			} else {
				// 데이터가 없는 경우 기본 값으로
				categoryMedianList.add(new AvgConsumptionGoalDto(category.getId(), 0.0));
			}
		}
		return categoryMedianList;
	}

	private double calculateMedian(List<Double> values) {

		/*
		 * values 리스트에서 0 보다 큰(소비 금액이 존재하는) 값만 필터링
		 * size 에 필터링한 값의 개수를 저장
		 * 홀수일 경우 size / 2 (가운데) 인덱스에 해당하는 값 반환
		 * 짝수일 경우 와 size/ 2 -1 인덱스 데이터와 size / 2의 인덱스 데이터의 평균을 처리
		 */

		List<Double> filteredValues = values.stream().filter(value -> value > 0).collect(Collectors.toList());

		int size = filteredValues.size();

		if (size == 0) {
			return 0.0;
		}
		Collections.sort(filteredValues);

		if (size % 2 == 0) {
			return (filteredValues.get(size / 2 - 1) + filteredValues.get(size / 2)) / 2.0;
		} else {
			return filteredValues.get(size / 2);
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

		updateGoalMapWithPrevious(userId, goalMonth, goalMap);
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

	private void updateGoalMapWithPrevious(Long userId, LocalDate goalMonth,
		Map<Long, ConsumptionGoalResponseDto> goalMap) {
		goalMap.keySet()
			.stream()
			.map(categoryId -> consumptionGoalRepository.findLatelyGoal(userId, categoryId, goalMonth.minusMonths(1)))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(consumptionGoalConverter::toConsumptionGoalResponseDtoFromPreviousGoal)
			.forEach(goal -> goalMap.put(goal.getCategoryId(), goal));
	}

	private void updateGoalMapWithCurrentMonth(Long userId, LocalDate goalMonth,
		Map<Long, ConsumptionGoalResponseDto> goalMap) {
		consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(userId, goalMonth)
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
		ConsumptionGoal previousConsumptionGoal = consumptionGoalRepository.findLatelyGoal(user.getId(),
				expense.getCategory().getId(), expense.getExpenseDate().toLocalDate().withDayOfMonth(1))
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
	public void decreaseConsumeAmount(Long userId, Long categoryId, Long amount, LocalDate expenseDate) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Not found user"));

		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new IllegalArgumentException("Not found Category"));

		LocalDate goalMonth = expenseDate.withDayOfMonth(1);
		ConsumptionGoal consumptionGoal = consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(
			user, category, goalMonth).orElseThrow(() -> new IllegalArgumentException("Not found ConsumptionGoal"));

		consumptionGoal.decreaseConsumeAmount(amount);
		consumptionGoalRepository.save(consumptionGoal);
	}

	// 현재 월이 아닌 이전 소비 내역에 대해서 소비 목표를 생성해야되는 경우 updateOrCreateDeletedConsumptionGoal 사용
	@Override
	@Transactional
	public void updateOrCreateDeletedConsumptionGoal(Long userId, Long categoryId, LocalDate goalMonth, Long amount) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

		// 해당 월의 ConsumptionGoal이 존재하는지 확인
		Optional<ConsumptionGoal> existingGoal = consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(
			user, category, goalMonth);

		if (existingGoal.isPresent()) {    // 존재하는 경우, consumeAmount 업데이트
			ConsumptionGoal consumptionGoal = existingGoal.get();
			consumptionGoal.updateConsumeAmount(amount);
			consumptionGoalRepository.save(consumptionGoal);
		} else {    // 존재하지 않는 경우, 새로운 ConsumptionGoal을 생성 (이 때 목표 금액은 0)
			ConsumptionGoal newGoal = ConsumptionGoal.builder()
				.user(user)
				.category(category)
				.goalMonth(goalMonth)
				.consumeAmount(amount)
				.goalAmount(0L)
				.build();

			newGoal.updateConsumeAmount(amount); // 신규 생성된 목표에 소비 금액 추가
			consumptionGoalRepository.save(newGoal);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public MonthReportResponseDto getMonthReport(Long userId) {   // 이번 달 비교 분석 레포트 (표정 변화, 멘트)

		User user = findUserById(userId);

		// 카테고리별 소비금액과 목표금액 리스트로 반환
		List<ConsumeAmountAndGoalAmountDto> dtoList = consumptionGoalRepository.findConsumeAmountAndGoalAmount(user,
			currentMonth);

		Map<Long, Long> consumeRatioByCategories = consumptionRate(
			dtoList);     // 카테고리별 소비 비율 리스트 (소비 금액 / 목표 금액 * 100)

		Long remainDaysRatio = dateRatio();    // 현재 날짜 기준 이번 달 날짜 비율 (예시 9월 20일 -> 20/30 * 100 = 33%)

		// 두 비율의 차를 통해 카테고리별 표정 변화 로직
		Map<Long, String> facialExpressionByCategoryId = updateFacialExpressionByCategoryId(consumeRatioByCategories,
			remainDaysRatio);

		String facialExpression = getMainFacialExpression(facialExpressionByCategoryId);

		String mainComment = getMainComment(dtoList);

		return consumptionGoalConverter.toMonthReportResponseDto(facialExpression, mainComment);
	}

	private Map<Long, Long> consumptionRate(List<ConsumeAmountAndGoalAmountDto> list) {

		Map<Long, Long> consumeRatio = new HashMap<>();
		for (ConsumeAmountAndGoalAmountDto dto : list) {

			Double cAmount = Double.valueOf(dto.getConsumeAmount());
			Double gAmount = Double.valueOf(dto.getGoalAmount());

			Long ratio = (long)((cAmount / gAmount) * 100);           // 소비 금액 / 목표 금액 * 100
			consumeRatio.put(dto.getCategoryId(), ratio);
		}
		return consumeRatio;
	}

	private Long dateRatio() {

		Double lastDayOfMonth = (double)LocalDate.now().lengthOfMonth();     // 이번 달 마지막 일
		Double nowDayOfMonth = (double)LocalDate.now().getDayOfMonth();   // 현재 일

		return (long)((nowDayOfMonth / lastDayOfMonth) * 100);   // 현재 일 / 마지막 일 * 100
	}

	private Map<Long, String> updateFacialExpressionByCategoryId(Map<Long, Long> consumeRatioByCategories,
		Long remainDaysRatio) {

		Map<Long, String> facialList = new HashMap<>();

		// 카테고리 별 소비 비율의 차로 표정 로직 업데이트
		/**
		 • 성공: 소비 비율이 남은 시간 비율보다 5% 이상 적음 (사용 속도가 매우 느림)
		 • 잘 지키고 있음: 소비 비율이 남은 시간 비율보다 0~5% 정도 차이 (적절한 소비)
		 • 기본: 소비 비율이 남은 시간 비율보다 0~10% 정도 높음 (소비가 조금 빠름)
		 • 아슬아슬함: 소비 비율이 남은 시간 비율보다 10~20% 높음 (조금 더 신경 써야 함)
		 • 위기: 소비 비율이 남은 시간 비율보다 20~30% 높음 (위험한 수준)
		 • 실패: 소비 비율이 남은 시간 비율보다 30% 이상 높음 (예산 초과 가능성 큼)
		 */
		for (Long key : consumeRatioByCategories.keySet()) {
			long ratioDifference = consumeRatioByCategories.get(key) - remainDaysRatio;

			if (ratioDifference <= -5) {
				facialList.put(key, "성공");
			} else if (ratioDifference <= 0) {
				facialList.put(key, "잘 지키고 있음");
			} else if (ratioDifference <= 10) {
				facialList.put(key, "기본");
			} else if (ratioDifference <= 20) {
				facialList.put(key, "아슬아슬함");
			} else if (ratioDifference <= 30) {
				facialList.put(key, "위기");
			} else {
				facialList.put(key, "실패");
			}
		}
		return facialList;
	}

	private String getMainFacialExpression(Map<Long, String> facialList) {

		if (facialList.containsValue("실패")) {
			return "실패";
		} else if (facialList.containsValue("위기")) {
			return "위기";
		} else if (facialList.containsValue("아슬아슬함")) {
			return "아슬아슬함";
		} else if (facialList.containsValue("기본")) {
			return "기본";
		} else if (facialList.containsValue("잘 지키고 있음")) {
			return "잘 지키고 있음";
		} else {
			return "성공";
		}
	}

	private String getMainComment(List<ConsumeAmountAndGoalAmountDto> list) {

		long minDifference = Long.MAX_VALUE;
		long minCategoryId = -1L;

		int remainDays = LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth();

		for (ConsumeAmountAndGoalAmountDto dto : list) {
			Long cAmount = dto.getConsumeAmount();
			Long gAmount = dto.getGoalAmount();

			long differenceAmount = gAmount - cAmount;

			// 차이가 가장 적은 값을 찾기
			if (differenceAmount < minDifference) {
				minDifference = differenceAmount;
				minCategoryId = dto.getCategoryId();
			}
		}
		Optional<Category> minCategory = categoryRepository.findById(minCategoryId);
		if (minCategory.isEmpty()) {
			throw new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다.");
		}

		String minCategoryName = minCategory.get().getName();

		long todayAvailableConsumptionAmount = minDifference / remainDays;
		long weekAvailableConsumptionAmount = todayAvailableConsumptionAmount * 7;

		log.info(String.valueOf(weekAvailableConsumptionAmount));

		NumberFormat nf = NumberFormat.getInstance(Locale.KOREA);  // 한국 단위로 locale

		if (weekAvailableConsumptionAmount < 0) {
			return "이번 달에는 " + minCategoryName + "에 " + Math.abs(minDifference) / 10000 + "만원 이상 초과했어요!";
		} else if (weekAvailableConsumptionAmount <= 10000) {
			return "이번 달에는 " + minCategoryName + "에 " + nf.format(weekAvailableConsumptionAmount / 1000 * 1000)
				+ "원 이상 쓰시면 안 돼요!";
		} else {
			return "이번 주에는 " + minCategoryName + "에 " + Math.abs(Math.abs(weekAvailableConsumptionAmount) / 10000)
				+ "만원 이상 쓰시면 안 돼요!";
		}
	}
}