package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.dto.ResponseDto;
import com.nl.sprinterbe.dto.SignUpRequestDto;
import com.nl.sprinterbe.dto.SignUpResponseDto;
import com.nl.sprinterbe.exception.LoginFormException;
import com.nl.sprinterbe.service.RefreshTokenService;
import com.nl.sprinterbe.dto.UserDTO;
import com.nl.sprinterbe.service.UserService;
import com.nl.sprinterbe.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = userService.findUser(Long.parseLong(authentication.getName()));
        return name + "님! Sprinter에 오신 것을 환영합니다!";
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable("user_id") Long userId, @RequestBody UserDTO userDTO) {
        userService.updateUser(userId, userDTO);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

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
        System.out.println("refresh1 = " + refresh1);

        return refresh1;
    }
}
