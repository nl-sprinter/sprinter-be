package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.dto.ResponseDto;
import com.nl.sprinterbe.dto.SignUpRequestDto;
import com.nl.sprinterbe.dto.SignUpResponseDto;
import com.nl.sprinterbe.exception.LoginFormException;
import com.nl.sprinterbe.dto.UserDTO;
import com.nl.sprinterbe.service.UserService;
import com.nl.sprinterbe.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class JoinController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.status(HttpStatus.OK).body("Check successful");
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> join(@RequestBody @Validated SignUpRequestDto request){
        userService.join(request);
        SignUpResponseDto response = new SignUpResponseDto("Success");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //AccessToken은 프론트쪽에서 지워버리고 RefreshToken만 받아 DB에서 삭제
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) throws LoginFormException {
        String refreshToken = jwtUtil.getRefreshToken(request);
        return userService.logout(refreshToken);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response){
        String refresh= jwtUtil.getRefreshToken(request);
        ResponseEntity<ResponseDto<?>> refresh1 = userService.refresh(refresh, response);
        return refresh1;
    }
}
