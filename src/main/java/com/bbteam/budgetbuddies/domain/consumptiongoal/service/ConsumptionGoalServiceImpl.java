package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.TopCategoryConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;
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
	private final TopCategoryConverter topCategoryConverter;
	private final UserRepository userRepository;

	private int peerAgeStartByUser;
	private int peerAgeEndByUser;

	@Override
	@Transactional(readOnly = true)
	public List<TopGoalCategoryResponseDTO> getTopGoalCategories(int top, Long userId, int peerAgeStart, int peerAgeEnd,
		String peerGender) {

		Gender gender = convertToGender(peerGender);

		Optional<User> user = userRepository.findById(userId);

		if (user.isEmpty()) {
			throw new NoSuchElementException("유저를 찾을 수 없습니다.");
		}

		if (peerAgeStart == 0 || peerAgeEnd == 0 || gender == Gender.NONE) {

			gender = user.get().getGender();
			setAgeGroupByUser(user.get().getAge());

		} else {
			peerAgeStartByUser = peerAgeStart;
			peerAgeEndByUser = peerAgeEnd;
		}

		log.info("peerAgeStartByUser: {}", peerAgeStartByUser);
		log.info("peerAgeStartByUser: {}", peerAgeEndByUser);
		log.info("peerAgeStartByUser: {}", gender);

		List<ConsumptionGoal> topGoals = consumptionGoalRepository.findTopCategoriesAndGoalAmount(top,
			peerAgeStartByUser, peerAgeEndByUser, gender);
		return topGoals.stream()// 여기서 top 개수만큼 제한
			.map(topCategoryConverter::fromEntity).collect(Collectors.toList());
	}

	private Gender convertToGender(String genderString) {
		try {
			return Gender.valueOf(genderString.toUpperCase());
		} catch (IllegalArgumentException e) {
			return Gender.NONE; // default or invalid value
		}
	}

	private void setAgeGroupByUser(int userAge) {
		if (userAge >= 20 && userAge <= 22) {
			peerAgeStartByUser = 20;
			peerAgeEndByUser = 22;
		} else if (userAge >= 23 && userAge <= 25) {
			peerAgeStartByUser = 23;
			peerAgeEndByUser = 25;
		} else if (userAge >= 26 && userAge <= 28) {
			peerAgeStartByUser = 26;
			peerAgeEndByUser = 28;
		} else if (userAge >= 29) {
			peerAgeStartByUser = 29;
			peerAgeEndByUser = 100;
		}
	}
}
