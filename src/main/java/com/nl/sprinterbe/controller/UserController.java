package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.dto.ProjectDto;
import com.nl.sprinterbe.dto.UserDto;
import com.nl.sprinterbe.service.UserService;
import com.nl.sprinterbe.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserDto userDTO, HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        userService.updateUser(userId, userDTO);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }
    //유저가 속한 프로젝트 가져오기
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDto>> getUserProjects(HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        List<ProjectDto> projects = userService.getProjects(userId);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }
}
