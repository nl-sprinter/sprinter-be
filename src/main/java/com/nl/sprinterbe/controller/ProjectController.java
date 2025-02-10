package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.dto.ProjectDTO;
import com.nl.sprinterbe.service.ProjectService;
import com.nl.sprinterbe.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    //프로젝트 생성
    @PostMapping("/create")
    public ResponseEntity<String> createProject(@RequestBody ProjectDTO projectDTO, @RequestParam Long userId) {
        projectService.createProject(projectDTO, userId);
        return ResponseEntity.status(201).body("Project created successfully");
    }

    //프로젝트 유저추가
    @PostMapping("/addUser")
    public ResponseEntity<String> addUserToProject(@RequestBody UserDTO userDTO, @RequestParam Long projectId) {
        projectService.addUserToProject( userDTO,projectId);
        return ResponseEntity.status(201).body("User added to project successfully");
    }

    //프로젝트 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProject(@RequestParam Long projectId, @RequestParam Long userId) {
        projectService.deleteProject(projectId, userId);
        return ResponseEntity.status(200).body("Project deleted successfully");
    }

}
