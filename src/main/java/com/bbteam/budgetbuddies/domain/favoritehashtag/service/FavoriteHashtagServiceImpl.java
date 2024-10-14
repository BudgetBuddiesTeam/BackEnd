package com.bbteam.budgetbuddies.domain.favoritehashtag.service;

import com.bbteam.budgetbuddies.domain.connectedinfo.entity.ConnectedInfo;
import com.bbteam.budgetbuddies.domain.connectedinfo.repository.ConnectedInfoRepository;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.favoritehashtag.dto.FavoriteHashtagResponseDto;
import com.bbteam.budgetbuddies.domain.favoritehashtag.entity.FavoriteHashtag;
import com.bbteam.budgetbuddies.domain.favoritehashtag.repository.FavoriteHashtagRepository;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteHashtagServiceImpl implements FavoriteHashtagService {

    private final ConnectedInfoRepository connectedInfoRepository;
    private final FavoriteHashtagRepository favoriteHashtagRepository;
    private final UserRepository userRepository;

    @Override
    public List<Long> getUsersForHashtag(Long discountInfoId, Long supportInfoId) {
        List<ConnectedInfo> connectedInfos;

        if (discountInfoId != null) {
            DiscountInfo discountInfo = DiscountInfo.withId(discountInfoId);
            connectedInfos = connectedInfoRepository.findAllByDiscountInfo(discountInfo);
        } else if (supportInfoId != null) {
            SupportInfo supportInfo = SupportInfo.withId(supportInfoId);
            connectedInfos = connectedInfoRepository.findAllBySupportInfo(supportInfo);
        } else {
            throw new IllegalArgumentException("discountInfoId 또는 supportInfoId 중 하나는 필수입니다.");
        }

        List<Long> hashtagIds = connectedInfos.stream()
                .map(connectedInfo -> connectedInfo.getHashtag().getId())
                .collect(Collectors.toList());
        System.out.println("Connected Hashtags IDs: " + hashtagIds);


        List<FavoriteHashtag> favoriteHashtags = favoriteHashtagRepository.findByHashtagIdIn(hashtagIds);
        System.out.println("Favorite Hashtags: " + favoriteHashtags);

        return favoriteHashtags.stream()
                .map(favoriteHashtag -> favoriteHashtag.getUser().getId())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<FavoriteHashtagResponseDto> findUsersByHashtag(Long discountInfoId, Long supportInfoId) {
        List<Long> userIds = getUsersForHashtag(discountInfoId, supportInfoId);

        return userIds.stream()
                .map(userId -> {
                    Optional<User> optionalUser = userRepository.findById(userId);
                    optionalUser.ifPresent(user -> System.out.println("User found: " + user)); // 여기에 추가
                    return optionalUser.map(FavoriteHashtagResponseDto::new)
                            .orElseGet(() -> {
                                System.out.println("User not found with id: " + userId);
                                return null;
                            });
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
