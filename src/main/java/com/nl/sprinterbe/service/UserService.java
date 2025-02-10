package com.nl.sprinterbe.service;

import com.nl.sprinterbe.common.ResponseStatus;
import com.nl.sprinterbe.dto.ResponseDto;
import com.nl.sprinterbe.dto.SignUpRequestDto;
import com.nl.sprinterbe.entity.RefreshToken;
import com.nl.sprinterbe.exception.LoginFormException;
import com.nl.sprinterbe.repository.RefreshTokenRepository;
import com.nl.sprinterbe.dto.UserDTO;
import com.nl.sprinterbe.entity.User;
import com.nl.sprinterbe.repository.UserRepository;
import com.nl.sprinterbe.util.JwtUtil;
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
    private final JwtUtil jwtUtil;

    public void updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        //시큐리티context에 있는 유저정보를 업데이트

        user.setNickname(userDTO.getNickname());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDTO.builder()
                        .userId(user.getUserId())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
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
        // DB에서 Refresh Token 조회
        Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findMatchingRefreshToken(id, refreshToken);
        Optional<User> userOpt = userRepository.findById(Long.parseLong(id));

        if (refreshTokenOpt.isEmpty() || userOpt.isEmpty()) {
            throw new JwtException("Invalid Refresh Token");
        }

        RefreshToken refreshTokenEntity = refreshTokenOpt.get();
        String newRefreshToken = jwtUtil.createRefreshJwt(id); // 새 Refresh Token 생성

        User user = userOpt.get();
        String newAccessToken = jwtUtil.createJwt(id,user.getEmail());

        refreshTokenEntity.setExpired(true); // 기존 refreshToken Expire 필드 값 true로 변경
        refreshTokenService.save(newRefreshToken,id); // 새로운 refreshToken DB에 저장

        response.setHeader("Authorization","Bearer "+ newAccessToken);
        response.addCookie(jwtUtil.createCookie("Refresh",newRefreshToken));
        return ResponseDto.settingResponse(HttpStatus.CREATED, ResponseStatus.TOKEN_CREATED);
    }
}
