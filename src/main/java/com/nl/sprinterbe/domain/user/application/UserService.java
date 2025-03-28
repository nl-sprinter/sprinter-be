package com.nl.sprinterbe.domain.user.application;

import com.nl.sprinterbe.domain.backlogcomment.dao.BacklogCommentRepository;
import com.nl.sprinterbe.domain.dailyscrum.dao.UserDailyScrumRepository;
import com.nl.sprinterbe.domain.project.dto.ProjectResponse;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.task.dao.TaskRepository;
import com.nl.sprinterbe.domain.task.entity.Task;
import com.nl.sprinterbe.domain.user.dto.UserInfoResponse;
import com.nl.sprinterbe.domain.user.dto.UserUpdateRequest;
import com.nl.sprinterbe.domain.userbacklog.dao.UserBacklogRepository;
import com.nl.sprinterbe.domain.userproject.entity.UserProject;
import com.nl.sprinterbe.domain.refreshtoken.application.RefreshTokenService;
import com.nl.sprinterbe.domain.user.dao.SignUpRequestDto;
import com.nl.sprinterbe.domain.refreshtoken.entity.RefreshToken;
import com.nl.sprinterbe.global.exception.user.LoginFormException;
import com.nl.sprinterbe.domain.refreshtoken.dao.RefreshTokenRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.userproject.dao.UserProjectRepository;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.global.exception.user.UserAlreadyExistsException;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
import com.nl.sprinterbe.global.exception.user.UserPasswordNotEqualsException;
import com.nl.sprinterbe.global.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserProjectRepository userProjectRepository;
    private final JwtUtil jwtUtil;
    // 댓글
    private final BacklogCommentRepository backlogCommentRepository;
    // 유저 백로그
    private final UserDailyScrumRepository userDailyScrumRepository;
    // Task
    private final TaskRepository taskRepository;
    // UserBacklog
    private final UserBacklogRepository userBacklogRepository;


    public void updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        //시큐리티context에 있는 유저정보를 업데이트
        String password = user.getPassword();
        String userPassword = userUpdateRequest.getCurrentPassword();
        if (!passwordEncoder.matches(userPassword, password)) {
            throw new UserPasswordNotEqualsException();
        }
        user.setNickname(userUpdateRequest.getNickname());
        user.setPassword(passwordEncoder.encode(userUpdateRequest.getNewPassword()));
    }

    // JWT 로 회원가입
    public void join(SignUpRequestDto signUpRequestDto) {
        String email = signUpRequestDto.getEmail();
        if (userRepository.findByEmailAndProvider(email, "LOCAL").isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = User.builder()
                .provider("LOCAL")
                .email(signUpRequestDto.getEmail())
                .nickname(signUpRequestDto.getNickname())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
    }

    //jwt로그아웃
    public ResponseEntity<Void> logout(String refreshToken) throws LoginFormException {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByRefresh(refreshToken);

        if (refreshTokenOpt.isPresent() && refreshToken != null && !refreshTokenOpt.get().getExpired()) {
            refreshTokenService.updateExpiredTokens(jwtUtil.getId(refreshToken));
            return ResponseEntity.ok().build();
        } else {
            throw new LoginFormException();
        }
    }

    public ResponseEntity<Void> refresh(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new JwtException("Refresh Null");
        }
        jwtUtil.isExpired(refreshToken);

        String id = jwtUtil.getId(refreshToken);
        RefreshToken refreshTokenOpt = refreshTokenRepository.findByRefresh(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh Token not found with id: " + id));
        User userOpt = userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UserNotFoundException());

        String newRefreshToken = jwtUtil.createRefreshJwt(id); // 새 Refresh Token 생성
        String newAccessToken = jwtUtil.createJwt(id, userOpt.getEmail());

        refreshTokenOpt.setExpired(true); // 기존 refreshToken Expire 필드 값 true로 변경
        refreshTokenService.save(newRefreshToken, id); // 새로운 refreshToken DB에 저장

        response.setHeader("Authorization", "Bearer " + newAccessToken);
//        response.addCookie(jwtUtil.createCookie("Refresh", newRefreshToken));

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * User 가 속한 프로젝트 가져오기
     */
    public List<ProjectResponse> getUserProjects(Long userId) {
        List<Project> projects = userProjectRepository.findByUserUserId(userId)
                .stream()
                .map(UserProject::getProject)
                .toList();

        return projects.stream()
                .map(project -> new ProjectResponse(project.getProjectId(), project.getProjectName(), project.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * 유저 정보 가져오기
     */
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserInfoResponse.of(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
