package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.user.dto.UserDTO;
import com.nl.sprinterbe.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 프로필 수정했다면 로그아웃하고 재접속시 새로 context생성돼서 새로운 정보로 바뀜
        return authentication.getName() + "님! Sprinter에 오신 것을 환영합니다!";
    }

    @PostMapping("/auth")
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
}
