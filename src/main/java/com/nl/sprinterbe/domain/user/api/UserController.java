package com.nl.sprinterbe.domain.user.api;

import com.nl.sprinterbe.domain.project.dto.ProjectDto;
import com.nl.sprinterbe.domain.user.dto.UserDetailResponse;
import com.nl.sprinterbe.domain.user.application.UserService;
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

    @Operation(summary = "유저 정보 수정", description = "유저 정보를 수정합니다.")
    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserDetailResponse userDetailResponse, HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        userService.updateUser(userId, userDetailResponse);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }
    //유저가 속한 프로젝트 가져오기
    @Operation(summary = "유저 프로젝트 조회", description = "유저가 속한 프로젝트들을 조회합니다.")
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDto>> getUserProjects(HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        List<ProjectDto> projects = userService.getProjects(userId);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }
}
