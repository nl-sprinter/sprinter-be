package com.nl.sprinterbe.domain.user.application;

import com.nl.sprinterbe.domain.user.dto.UserDetailResponse;
import com.nl.sprinterbe.global.common.code.ResponseStatus;
import com.nl.sprinterbe.domain.refreshToken.application.RefreshTokenService;
import com.nl.sprinterbe.domain.project.dto.ProjectNameDto;
import com.nl.sprinterbe.global.common.ResponseDto;
import com.nl.sprinterbe.domain.user.dao.SignUpRequestDto;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.refreshToken.entity.RefreshToken;
import com.nl.sprinterbe.domain.userProject.entity.UserProject;
import com.nl.sprinterbe.global.exception.LoginFormException;
import com.nl.sprinterbe.domain.refreshToken.dao.RefreshTokenRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.userProject.dao.UserProjectRepository;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
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
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserProjectRepository userProjectRepository;
    private final JwtUtil jwtUtil;

    public void updateUser(Long userId, UserDetailResponse userDetailResponse) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        //시큐리티context에 있는 유저정보를 업데이트

        user.setNickname(userDetailResponse.getNickname());
        user.setPassword(passwordEncoder.encode(userDetailResponse.getPassword()));

        userRepository.save(user);
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

    public List<ProjectNameDto> getProjects(Long userId) {
        List<Project> projects = userProjectRepository.findByUserUserId(userId)
                .stream()
                .map(UserProject::getProject)
                .collect(Collectors.toList());

        return projects.stream()
                .map(project -> new ProjectNameDto(project.getProjectName(), project.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * User 닉네임 가져오기
     */
    public String getNickname(Long userId) {
        return userRepository.findById(userId).orElseThrow().getNickname();
    }
}
