package com.nl.sprinterbe.domain.user.api;

import com.nl.sprinterbe.domain.project.dto.ProjectResponse;
import com.nl.sprinterbe.domain.user.application.UserService;
import com.nl.sprinterbe.domain.user.dto.UserInfoResponse;
import com.nl.sprinterbe.domain.user.dto.UserUpdateRequest;
import com.nl.sprinterbe.global.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "유저 관련 API 입니다.")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "유저 정보 수정", description = "유저 닉네임과 비밀번호를 수정합니다.") // 프론트 연동 OK
    @PatchMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }


    @Operation(summary = "프로젝트 조회", description = "유저가 속한 프로젝트들을 조회합니다.") // 프론트 연동 OK
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponse>> getUserProjects(HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        List<ProjectResponse> projects = userService.getUserProjects(userId);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }


    @Operation(summary = "유저 정보 조회", description = "클라이언트에서 유저의 상태 관리를 위해 유저 정보를 조회합니다.") // 프론트 연동 OK
    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        UserInfoResponse userInfo = userService.getUserInfo(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

    @Operation(summary = "회원탈퇴", description = "회원을 탈퇴합니다.") // 프론트 연동 OK
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
