package com.nl.sprinterbe.domain.user.api;

import com.nl.sprinterbe.domain.user.dao.SignUpRequestDto;
import com.nl.sprinterbe.domain.user.dao.SignUpResponseDto;
import com.nl.sprinterbe.global.exception.LoginFormException;
import com.nl.sprinterbe.domain.user.application.UserService;
import com.nl.sprinterbe.global.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "인증 관련 API 입니다.")
public class JoinController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.status(HttpStatus.OK).body("Check successful");
    }

    @Operation(summary = "회원가입", description = "회원가입을 합니다.") // 프론트 연동 OK
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> join(@RequestBody @Validated SignUpRequestDto request){
        userService.join(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //AccessToken은 프론트쪽에서 지워버리고 RefreshToken만 받아 DB에서 삭제
    @Operation(summary = "로그아웃", description = "로그아웃을 합니다.") // 프론트 연동 OK
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) throws LoginFormException {
        String refreshToken = jwtUtil.getRefreshToken(request);
        return userService.logout(refreshToken);
    }

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.") // 프론트 연동 OK
    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response){
        String refresh= jwtUtil.getRefreshToken(request);
        return userService.refresh(refresh, response);
    }
}
