package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.dto.ProjectDTO;
import com.nl.sprinterbe.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    //프로젝트 생성(gpt없이)
    @PostMapping("/project/create")
    public ResponseEntity<String> createProject(@RequestBody ProjectDTO projectDTO) {
        projectService.createProject(projectDTO);
        return ResponseEntity.status(201).body("Project created successfully");
    }

    //프로젝트 생성(gpt있이)


    //프로젝트 수정
    @PutMapping("/project/{project_id}")
    public ResponseEntity<String> updateProject(@PathVariable("project_id") Long projectId, @RequestBody ProjectDTO projectDTO) {
        projectService.updateProject(projectId, projectDTO);
        return ResponseEntity.status(200).body("Project updated successfully");
    }

    //프로젝트 삭제
    @DeleteMapping("/project/{project_id}")
    public ResponseEntity<String> deleteProject(@PathVariable("project_id") Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.status(200).body("Project deleted successfully");
    }

    //프로젝트 리스트 조회
    @GetMapping("/projects")
    public List<ProjectDTO> getProjects() {
        return projectService.getProjects();
    }

}
