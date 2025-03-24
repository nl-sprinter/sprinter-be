package com.nl.sprinterbe.domain.user.dao;

import com.nl.sprinterbe.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndProvider(String email,String provider);

    Page<User> findByEmailContainingOrNicknameContaining(String email, String nickname, Pageable pageable);

    // 유저 닉네임 중복 여부
    boolean existsByNickname(String nickname);
}
