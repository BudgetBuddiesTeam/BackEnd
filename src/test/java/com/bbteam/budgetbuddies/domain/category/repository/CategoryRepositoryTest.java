package com.bbteam.budgetbuddies.domain.category.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;

@DisplayName("Category 레포지토리 테스트의 ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	CategoryRepository categoryRepository;

	@Test
	@DisplayName("Custom 카테고리와 Base 카테고리 조회 성공")
	void findUserCategoryByUserIdTest_Success() {
		User user = userRepository.save(User.builder()
			.email("email")
			.age(24)
			.name("name")
			.phoneNumber("010-1234-5678")
			.build());

		User otherUser = userRepository.save(User.builder()
			.email("email2")
			.age(25)
			.name("name2")
			.phoneNumber("010-2345-5678")
			.build());

		Category defaultCategory = categoryRepository.save(Category.builder()
			.name("디폴트 카테고리")
			.user(null)
			.isDefault(true)
			.build());

		Category userCategory = categoryRepository.save(Category.builder()
			.name("유저 카테고리")
			.user(user)
			.isDefault(false)
			.build());

		categoryRepository.save(Category.builder()
			.name("다른 유저 카테고리")
			.user(otherUser)
			.isDefault(false)
			.build());

		// when
		List<Category> result = categoryRepository.findUserCategoryByUserId(user.getId());

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(List.of(defaultCategory, userCategory));
	}
}