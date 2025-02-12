package com.nl.sprinterbe.repository;

import com.nl.sprinterbe.entity.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByRefresh(String refresh);

    // refresh 값으로 RefreshToken을 조회
    Optional<RefreshToken> findByRefresh(String refresh);

    // userId로 expired 필드가 false인 RefreshToken 리스트 조회
    List<RefreshToken> findByUserIdAndExpiredFalse(String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RefreshToken> findByRefreshAndUserIdAndExpiredFalse(String refresh, String userId);
}
