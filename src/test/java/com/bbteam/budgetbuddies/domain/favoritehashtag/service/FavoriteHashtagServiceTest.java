package com.bbteam.budgetbuddies.domain.favoritehashtag.service;

import com.bbteam.budgetbuddies.domain.connectedinfo.entity.ConnectedInfo;
import com.bbteam.budgetbuddies.domain.connectedinfo.repository.ConnectedInfoRepository;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.repository.DiscountInfoRepository;
import com.bbteam.budgetbuddies.domain.favoritehashtag.dto.FavoriteHashtagResponseDto;
import com.bbteam.budgetbuddies.domain.favoritehashtag.entity.FavoriteHashtag;
import com.bbteam.budgetbuddies.domain.favoritehashtag.repository.FavoriteHashtagRepository;
import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import com.bbteam.budgetbuddies.domain.hashtag.repository.HashtagRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class FavoriteHashtagServiceTest {

    @Autowired
    private FavoriteHashtagRepository favoriteHashtagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private FavoriteHashtagService favoriteHashtagService;

    @Autowired
    private ConnectedInfoRepository connectedInfoRepository;

    @Autowired
    private DiscountInfoRepository discountInfoRepository;

    private User user;
    private Hashtag hashtag;
    private DiscountInfo discountInfo;

    @BeforeEach
    void setUp() {
        // 데이터 초기화
        connectedInfoRepository.deleteAll();
        favoriteHashtagRepository.deleteAll();
        discountInfoRepository.deleteAll();
        hashtagRepository.deleteAll();
        userRepository.deleteAll();

        // Given: 사용자와 해시태그, 할인정보를 생성
        user = userRepository.save(User.builder()
                .phoneNumber("01012345678")
                .name("Test User")
                .age(25)
                .email("test1@example.com")
                .build());

        hashtag = hashtagRepository.save(new Hashtag("식비"));
        discountInfo = discountInfoRepository.save(DiscountInfo.withId(1L));

        favoriteHashtagRepository.save(FavoriteHashtag.builder().user(user).hashtag(hashtag).build());

        connectedInfoRepository.save(ConnectedInfo.builder()
                .discountInfo(discountInfo)
                .hashtag(hashtag)
                .build());
    }

    @Test
    void testSaveFavoriteHashtags() {
        // When: 사용자가 관심 있는 해시태그를 선택하여 저장함
        FavoriteHashtag favorite1 = FavoriteHashtag.builder().user(user).hashtag(hashtag).build();
        favoriteHashtagRepository.save(favorite1);

        // Then: FavoriteHashtag에 잘 저장되었는지 검증
        List<FavoriteHashtag> favorites = favoriteHashtagRepository.findByUser(user);
        assertThat(favorites).hasSize(1);
        assertThat(favorites).extracting(fav -> fav.getHashtag().getName())
                .containsExactly("식비");
    }

    @Test
    void testFindUsersByHashtagForDiscountInfo() {
        // When: 할인정보에 연결된 해시태그를 기반으로 사용자를 조회
        List<FavoriteHashtagResponseDto> userResponses = favoriteHashtagService.findUsersByHashtag(discountInfo.getId(), hashtag.getId());

        // Then: 해당 해시태그를 가진 유저가 응답되는지 확인
        assertThat(userResponses).hasSize(1);
        assertThat(userResponses.get(0).getUserId()).isEqualTo(user.getId());
    }
}
