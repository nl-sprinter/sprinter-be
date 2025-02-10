package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.dto.ResponseDto;
import com.nl.sprinterbe.dto.SignUpRequestDto;
import com.nl.sprinterbe.dto.SignUpResponseDto;
import com.nl.sprinterbe.entity.RefreshToken;
import com.nl.sprinterbe.exception.DuplicateEmailException;
import com.nl.sprinterbe.exception.LoginFormException;
import com.nl.sprinterbe.service.RefreshTokenService;
import com.nl.sprinterbe.user.dto.UserDTO;
import com.nl.sprinterbe.user.entity.User;
import com.nl.sprinterbe.user.service.JoinService;
import com.nl.sprinterbe.user.service.UserService;
import com.nl.sprinterbe.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import com.nl.sprinterbe.common.ResponseStatus;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JoinService joinService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


    @GetMapping("/hello")
    public String hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 프로필 수정했다면 로그아웃하고 재접속시 새로 context생성돼서 새로운 정보로 바뀜
        return authentication.getName() + "님! Sprinter에 오신 것을 환영합니다!";
    }

    @PostMapping("/signin")
    public ResponseEntity<String> auth(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return ResponseEntity.status(201).body("Auth successful");
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable("user_id") Long userId, @RequestBody UserDTO userDTO) {
        userService.updateUser(userId, userDTO);
        return ResponseEntity.status(200).body("User updated successfully");
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.status(200).body("Check successful");
    }

    @PostMapping("/join")
    public ResponseEntity<SignUpResponseDto> join(@RequestBody @Validated SignUpRequestDto request){
        String email = request.getEmail();
        User user = userService.findByEmailAndProvider(email,"LOCAL");
        if(user!=null){
            throw new DuplicateEmailException("Email Duplicate");
        }
        joinService.join(request);
        SignUpResponseDto response = new SignUpResponseDto("Success");


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/query")
    public String query(){
        return "query";
    }

    //AccessToken은 프론트쪽에서 지워버리고 RefreshToken만 받아 DB에서 삭제
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) throws LoginFormException {
        //Refresh Token 뽑기
        String refreshToken = jwtUtil.getRefreshToken(request);
        String id = jwtUtil.getId(refreshToken);
        // 1단계: 해당 refresh 값이 DB에 있는지 확인
        Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByRefresh(refreshToken);
        System.out.println("================="+!refreshTokenOpt.get().getExpired());
        if (refreshTokenOpt.isPresent() && refreshToken!=null && !refreshTokenOpt.get().getExpired()) {

            // 2단계: 값이 있다면 삭제
            refreshTokenService.updateExpiredTokens(id);
            //return ResponseEntity.ok("Refresh token Expired successfully");
            return ResponseDto.settingResponse(HttpStatus.OK,ResponseStatus.LOGOUT_SUCCESS);
        } else {
            // 값이 없으면 삭제할 것이 없다는 응답
            //return ResponseEntity.status(404).body("Refresh token not found");
            throw new LoginFormException("Invalid Refresh Token");
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response){
        String refresh= jwtUtil.getRefreshToken(request);

        if(refresh==null){
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("refresh Token is null");
            throw new JwtException("Refresh Null");
        }

        jwtUtil.isExpired(refresh);


        String id = jwtUtil.getId(refresh);
        // DB에서 Refresh Token 조회
        Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findMatchingRefreshToken(id, refresh);
        Optional<User> userOpt = userService.findById(Long.parseLong(id));

        if (refreshTokenOpt.isEmpty() || userOpt.isEmpty()) {
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token not found");
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
