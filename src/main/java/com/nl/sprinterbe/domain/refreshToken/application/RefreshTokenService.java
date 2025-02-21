package com.nl.sprinterbe.domain.refreshToken.application;

import com.nl.sprinterbe.domain.refreshToken.entity.RefreshToken;
import com.nl.sprinterbe.domain.refreshToken.dao.RefreshTokenRepository;
import com.nl.sprinterbe.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

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
