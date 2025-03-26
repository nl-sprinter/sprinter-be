package com.nl.sprinterbe.domain.admin.application;

import com.nl.sprinterbe.domain.admin.dto.AlarmRequest;
import com.nl.sprinterbe.domain.admin.dto.UserRequest;
import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final UserRepository userRepository;

    List<User> users = userRepository.findAll();

    @Transactional(readOnly = true)
    public Page<UserRequest> getAllUsers(Pageable pageable) {
        // 페이징 처리된 사용자 목록 조회
        Page<User> usersPage = userRepository.findAll(pageable);

        // User 엔티티를 UserDto로 변환하여 반환
        return usersPage.map(UserRequest::of);
    }

    @Transactional(readOnly = true)
    public Page<UserRequest> searchUsers(String keyword, Pageable pageable) {
        Page<User> usersPage;

        if (!StringUtils.hasText(keyword)) {
            // 키워드가 없으면 전체 목록 반환
            usersPage = userRepository.findAll(pageable);
        } else {
            // 이메일 또는 닉네임으로 검색
            usersPage = userRepository.findByEmailContainingOrNicknameContaining(
                    keyword, keyword, pageable);
        }

        return usersPage.map(UserRequest::of);
    }

    public void deleteUser(List<Long> userIds) {
        userRepository.deleteAllById(userIds);
    }


}
