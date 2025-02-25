package com.nl.sprinterbe.domain.project.api;

import com.nl.sprinterbe.domain.project.dto.ProjectDto;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.dto.StartingDataDto;
import com.nl.sprinterbe.domain.project.application.ProjectService;
import com.nl.sprinterbe.domain.user.dto.UserDetailResponse;
import com.nl.sprinterbe.global.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
@Tag(name = "Project API", description = "프로젝트 관련 API 입니다.")
public class ProjectController {
    private final ProjectService projectService;
    private final JwtUtil jwtUtil;

    //프로젝트 생성
    @Operation(summary = "프로젝트 생성", description = "StartingDataDto 를 받아서 프로젝트를 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<String> createProject(@RequestBody StartingDataDto StartingDataDto, HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        projectService.createProject(StartingDataDto, userId);
        return ResponseEntity.status(201).body("Project created successfully"); // 수정필요.
    }

    //프로젝트 유저추가(일단 이메일 받아와서 추가하는식)
    @Operation(summary = "프로젝트 유저 추가", description = "프로젝트에 유저를 추가합니다.(현재는 이메일로 검색해서 추가)")
    @PostMapping("/addUser/{projectId}")
    public ResponseEntity<String> addUserToProject(@RequestBody UserDetailResponse userDetailResponse, @PathVariable Long projectId) {
        projectService.addUserToProject(userDetailResponse, projectId);
        return ResponseEntity.status(201).body("User added to project successfully");
    }

    //프로젝트 삭제
    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다.")
    @GetMapping("/delete/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId, HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        projectService.deleteProject(projectId, userId);
        return ResponseEntity.status(200).body("Project deleted successfully");
    }

    //유저 조회
    @Operation(summary = "프로젝트 유저 조회", description = "프로젝트에 속한 유저들을 조회합니다.")
    @GetMapping("/users/{projectId}")
    public ResponseEntity<List<UserDetailResponse>> getUsers(@PathVariable Long projectId) {
        List<UserDetailResponse> users = projectService.getUsers(projectId);
        return ResponseEntity.status(200).body(users);
    }

    //프로젝트 업데이트
    @Operation(summary = "프로젝트 업데이트", description = "프로젝트를 업데이트합니다.")
    @PostMapping("/update/{projectId}")
    public ResponseEntity<String> updateProject(@PathVariable Long projectId, @RequestBody ProjectDto projectDTO) {
        projectService.updateProject(projectId, projectDTO);
        return ResponseEntity.status(200).body("Project updated successfully");
    }


}
