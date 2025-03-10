package com.nl.sprinterbe.domain.user.application;

import com.nl.sprinterbe.domain.project.dto.ProjectResponse;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.user.dto.UserInfoResponse;
import com.nl.sprinterbe.domain.user.dto.UserUpdateRequest;
import com.nl.sprinterbe.domain.userProject.entity.UserProject;
import com.nl.sprinterbe.global.common.code.ResponseStatus;
import com.nl.sprinterbe.domain.refreshToken.application.RefreshTokenService;
import com.nl.sprinterbe.global.common.ResponseDto;
import com.nl.sprinterbe.domain.user.dao.SignUpRequestDto;
import com.nl.sprinterbe.domain.refreshToken.entity.RefreshToken;
import com.nl.sprinterbe.global.exception.LoginFormException;
import com.nl.sprinterbe.domain.refreshToken.dao.RefreshTokenRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.userProject.dao.UserProjectRepository;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.global.exception.user.DuplicateUserNicknameException;
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

    public void updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 비밀번호 검사
        // 비밀번호 비교할땐 .matches 써야함
        if (!passwordEncoder.matches(userUpdateRequest.getCurrentPassword(), findUser.getPassword())) {
            throw new UserPasswordNotEqualsException();
        }

        // 닉네임 중복검사
        boolean exists = userRepository.existsByNickname(findUser.getNickname());
        if (!userUpdateRequest.getNickname().equals(findUser.getNickname()) && exists) {
            throw new DuplicateUserNicknameException();
        }

        findUser.setNickname(userUpdateRequest.getNickname());
        findUser.setPassword(passwordEncoder.encode(userUpdateRequest.getNewPassword()));
    }

    //jwt로 회원가입
    public void join(SignUpRequestDto dto){
        String email = dto.getEmail();
        if(userRepository.findByEmailAndProvider(email,"LOCAL").isPresent()) {
            throw new RuntimeException("User with email " + email + " already exists");
        }
        User user = new User();
        user.setProvider("LOCAL");
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }

    //jwt로그아웃
    public ResponseEntity<ResponseDto<?>> logout(String refreshToken) throws LoginFormException {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByRefresh(refreshToken);

        if (refreshTokenOpt.isPresent() && refreshToken!=null && !refreshTokenOpt.get().getExpired()) {
            refreshTokenService.updateExpiredTokens(jwtUtil.getId(refreshToken));
            return ResponseDto.settingResponse(HttpStatus.OK, ResponseStatus.LOGOUT_SUCCESS);
        } else {
            throw new LoginFormException("Invalid Refresh Token");
        }
    }

    public ResponseEntity<ResponseDto<?>> refresh(String refreshToken, HttpServletResponse response){
        if(refreshToken==null){
            throw new JwtException("Refresh Null");
        }
        jwtUtil.isExpired(refreshToken);

        String id = jwtUtil.getId(refreshToken);
        RefreshToken refreshTokenOpt = refreshTokenRepository.findByRefreshAndUserIdAndExpiredFalse(refreshToken, id)
                .orElseThrow(() -> new RuntimeException("Refresh Token not found with id: " + id));
        User userOpt = userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UserNotFoundException());

        String newRefreshToken = jwtUtil.createRefreshJwt(id); // 새 Refresh Token 생성
        String newAccessToken = jwtUtil.createJwt(id,userOpt.getEmail());

        refreshTokenOpt.setExpired(true); // 기존 refreshToken Expire 필드 값 true로 변경
        refreshTokenService.save(newRefreshToken,id); // 새로운 refreshToken DB에 저장

        response.setHeader("Authorization","Bearer "+ newAccessToken);
        response.addCookie(jwtUtil.createCookie("Refresh",newRefreshToken));

        return ResponseDto.settingResponse(HttpStatus.CREATED, ResponseStatus.TOKEN_CREATED);
    }

    /**
     * User 가 속한 프로젝트 가져오기
     */
    public List<ProjectResponse> getUserProjects(Long userId) {
        List<Project> projects = userProjectRepository.findByUserUserId(userId)
                .stream()
                .map(UserProject::getProject)
                .collect(Collectors.toList());

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

    /**
     * 유저 삭제
     * 유령 유저 로직 등 도입 필요 TODO
     */
    public void deleteUser(Long userId) {
        // 다른 리포지토리에서 유저를 유령유저로 치환
        // 댓글..
        // 채팅..
        userRepository.deleteById(userId);
    }
}
