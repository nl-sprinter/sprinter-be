package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.dto.ProjectDTO;
import com.nl.sprinterbe.dto.UserDTO;
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
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        userService.updateUser(userId, userDTO);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }
    //프로젝트 가져오는거
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getUserProjects(HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        List<ProjectDTO> projects = userService.getProjects(userId);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }
}
