package com.bbteam.budgetbuddies.global.security.refreshtoken;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 리프레시 토큰을 저장하거나 갱신하는 메서드.
     * 해당 사용자의 리프레시 토큰이 이미 존재하면 새 토큰으로 갱신하고,
     * 존재하지 않으면 새 리프레시 토큰을 생성하여 저장합니다.
     *
     * @param user 사용자 정보
     * @param newToken 새로운 리프레시 토큰
     */
    public void saveOrUpdateRefreshToken(User user, String newToken) {
        // 해당 사용자의 리프레시 토큰이 이미 존재하는지 확인
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        // 기존 토큰이 있으면 갱신, 없으면 새로 저장
        if (existingToken.isPresent()) {
            // 기존 리프레시 토큰을 새로운 토큰으로 갱신
            existingToken.get().setToken(newToken);
            refreshTokenRepository.save(existingToken.get());
        } else {
            // 새 리프레시 토큰 생성 및 저장
            RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(newToken)
                .build();
            refreshTokenRepository.save(refreshToken);
        }
    }

    /**
     * 리프레시 토큰이 해당 사용자와 일치하는지 검증하는 메서드.
     * 사용자와 연관된 리프레시 토큰이 없으면 예외를 발생시키고,
     * 있으면 토큰이 일치하는지 여부를 반환합니다.
     *
     * @param user 해당 사용자
     * @param token 리프레시 토큰 문자열
     * @return 토큰이 사용자와 일치하는지 여부
     */
    public boolean validateRefreshToken(User user, String token) {
        // 사용자와 연결된 리프레시 토큰이 없으면 예외 발생
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
            .orElseThrow(() -> new GeneralException(ErrorStatus._TOKEN_NOT_FOUND));

        // 리프레시 토큰 문자열이 일치하는지 검증
        return refreshToken.getToken().equals(token);
    }

    /**
     * 리프레시 토큰을 삭제하는 메서드.
     * 사용자 정보를 기반으로 리프레시 토큰을 삭제합니다.
     *
     * @param user 사용자 정보
     */
    public void deleteRefreshToken(User user) {
        // 사용자와 연관된 리프레시 토큰 삭제
        refreshTokenRepository.deleteByUser(user);
    }
}