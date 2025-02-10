package com.nl.sprinterbe.service;

import com.nl.sprinterbe.entity.RefreshToken;
import com.nl.sprinterbe.repository.RefreshTokenRepository;
import com.nl.sprinterbe.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void addRefresh(String userId, String refresh){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, JwtUtil.REFRESH_TOKEN_HOURS);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setRefresh(refresh);
        refreshToken.setExpired(false);

        refreshTokenRepository.save(refreshToken);
    }

    //User의 Db id로 Refresh 테이블의 userId와 일치하는 토큰이 있는지 확인
    public Optional<RefreshToken> findMatchingRefreshToken(String userId, String refreshToken) {
        Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findByUserIdAndExpiredFalse(userId)
                .stream()
                .filter(token -> token.getRefresh().equals(refreshToken))
                .findFirst();

        return refreshTokenEntity;
    }

    public RefreshToken save(String refreshToken,String userId) {
        RefreshToken tokenObj = new RefreshToken();
        tokenObj.setExpired(false);
        tokenObj.setRefresh(refreshToken);
        tokenObj.setUserId(userId);
        return refreshTokenRepository.save(tokenObj);
    }

    public void updateExpiredTokens(String userId) {
        // userId로 expired가 false인 RefreshToken을 조회
        List<RefreshToken> tokens = refreshTokenRepository.findByUserIdAndExpiredFalse(userId);

        // 만약 tokens가 1개 이상이라면, expired 값을 true로 업데이트
        if (!tokens.isEmpty()) {
            for (RefreshToken token : tokens) {
                token.setExpired(true);  // expired 필드를 true로 설정
            }
        }
    }
}
