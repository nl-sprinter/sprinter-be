package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.dto.ProjectDto;
import com.nl.sprinterbe.service.ProjectService;
import com.nl.sprinterbe.dto.UserDto;
import com.nl.sprinterbe.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final JwtUtil jwtUtil;

    //프로젝트 생성
    @PostMapping("/create")
    public ResponseEntity<String> createProject(@RequestBody ProjectDto projectDTO, HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        projectService.createProject(projectDTO, userId);
        return ResponseEntity.status(201).body("Project created successfully");
    }

    //프로젝트 유저추가(일단 이메일 받아와서 추가하는식)
    @PostMapping("/addUser/{projectId}")
    public ResponseEntity<String> addUserToProject(@RequestBody UserDto userDTO, @PathVariable Long projectId) {
        projectService.addUserToProject(userDTO, projectId);
        return ResponseEntity.status(201).body("User added to project successfully");
    }

    //프로젝트 삭제
    @GetMapping("/delete/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId, HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        projectService.deleteProject(projectId, userId);
        return ResponseEntity.status(200).body("Project deleted successfully");
    }

    //유저 조회
    @GetMapping("/users/{projectId}")
    public ResponseEntity<List<UserDto>> getUsers(@PathVariable Long projectId) {
        List<UserDto> users = projectService.getUsers(projectId);
        return ResponseEntity.status(200).body(users);
    }

    //프로젝트 업데이트
    @PostMapping("/update/{projectId}")
    public ResponseEntity<String> updateProject(@PathVariable Long projectId, @RequestBody ProjectDto projectDTO) {
        projectService.updateProject(projectId, projectDTO);
        return ResponseEntity.status(200).body("Project updated successfully");
    }


}
