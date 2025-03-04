package com.nl.sprinterbe.domain.project.api;

import com.nl.sprinterbe.domain.backlog.application.BacklogService;
import com.nl.sprinterbe.domain.backlog.dto.*;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Project API", description = "프로젝트 관련 API 입니다.")
public class ProjectController {
    private final ProjectService projectService;
    private final BacklogService backlogService;
    private final JwtUtil jwtUtil;

    //프로젝트 생성
    @Operation(summary = "프로젝트 생성", description = "StartingDataDto 를 받아서 프로젝트를 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<String> createProject(@RequestBody StartingDataDto StartingDataDto, HttpServletRequest request) {
        Long userId = jwtUtil.removeBearerAndReturnId(request);
        projectService.createProject(StartingDataDto, userId);
        return ResponseEntity.status(201).body("Project created successfully"); // 수정필요.

//        return new ResponseEntity<>(projectService.createProject(StartingDataDto, userId), HttpStatus.CREATED);
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

    //------------------------------------------------------------------------


    //Sprint 자리


    //------------------------------------------------------------------------


//     /api/v1/projects/{projectId}/sprints/{sprintId}/backlogs
    //나의 Backlog + 나의 달성 현황
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/users/{userId}")
    public ResponseEntity<Slice<BacklogInfoResponse>> getBacklogInfoList(@PathVariable Long projectId, @PathVariable Long userId, @PageableDefault(size=5) Pageable pageable) {
        return ResponseEntity.ok(backlogService.findBacklogListByProjectId(projectId,userId,pageable));
    }

    //Backlog 정보
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}")
    public ResponseEntity<BacklogDetailResponse> getBacklogDetail(@PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.findBacklogDetailById(backlogId));
    }

    //Backlog에 걸려있는 유저
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users")
    public ResponseEntity<List<BacklogUserResponse>> getBacklogUser(@PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.findUserByBacklogId(backlogId));
    }

    //Backlog에 걸려있는 Task
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks")
    public ResponseEntity<List<BacklogTaskResponse>> getBacklogTask(@PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.findTaskByBacklogId(backlogId));
    }

    //Backlog에 걸려있는 이슈
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues")
    public ResponseEntity<List<BacklogIssueResponse>> getBacklogIssue(@PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.findIssueByBacklogId(backlogId));
    }

    //Backlog 추가 (한번에)
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs")
    public ResponseEntity<BacklogPostResponse> addBacklog(@RequestBody BacklogPostRequest request, @PathVariable Long sprintId){
        return ResponseEntity.ok(backlogService.createBacklog(request,sprintId));
    }


    //유저 수정하기 기능 중 현재 백로그에는 할당되어 있지 않지만 프로젝트에는 할당된 유저
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users-excluded")
    public ResponseEntity<List<BacklogUserResponse>> getBacklogExceptUsers(@PathVariable Long projectId,@PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.findBacklogExceptUsers(projectId,backlogId));
    }

    //백로그 제목 수정
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/title")
    public ResponseEntity<BacklogTitleResponse> updateBacklogTitle(@RequestBody BacklogTitleRequest request, @PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.updateBacklogTitle(request,backlogId));
    }

    //---------------------------------- 수정 시 한번에 -----------------------------------
    //백로그 유저 수정 (삭제 , 삽입)
    /*@PatchMapping("/{backlogId}/users")
    public ResponseEntity<List<BacklogUserResponse>> updateBacklogUsers(@PathVariable Long backlogId,
                                                     @RequestBody BacklogUserUpdateRequest request) {
        return ResponseEntity.ok(backlogService.updateBacklogUsers(backlogId, request));
    }

    //업무 수정 (삭제 , 삽입)
    @PatchMapping("/{backlogId}/tasks")
    public ResponseEntity<List<BacklogTaskResponse>> updateBacklogTasks(@PathVariable Long backlogId,
                                                     @RequestBody BacklogTaskUpdateRequest request) {
        return ResponseEntity.ok(backlogService.updateBacklogTasks(backlogId, request));
    }*/
    //---------------------------------- 수정 시 한번에 -----------------------------------


    //---------------------------------- 수정 시 하나씩(현재 채택) -----------------------------------
    //유저 Delete
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long backlogId,@PathVariable Long userId) {
        backlogService.deleteUser(backlogId,userId);
        return ResponseEntity.ok().build();
    }
    //유저 add
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users/{userId}")
    public ResponseEntity<BacklogUserResponse> addUser(@PathVariable Long backlogId , @PathVariable Long userId){
        return ResponseEntity.ok(backlogService.addBacklogUser(backlogId,userId));
    }

    //업무 Delete
    @DeleteMapping(("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}"))
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        backlogService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }
    //업무 add
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks")
    public ResponseEntity<BacklogTaskResponse> addTask(@PathVariable Long backlogId ,@RequestBody BacklogTaskRequest request) {
        return ResponseEntity.ok(backlogService.addTask(backlogId,request));
    }

    //---------------------------------- 수정 시 하나씩 -----------------------------------

    //이슈 수정
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}")
    public ResponseEntity<BacklogIssueResponse> updateIssue(@PathVariable Long issueId, @RequestBody BacklogIssueRequest request){
        return ResponseEntity.ok(backlogService.updateIssue(issueId,request));
    }

    //이슈 add
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues")
    public ResponseEntity<BacklogIssueResponse> addIssue(@PathVariable Long backlogId,@RequestBody BacklogIssueRequest request) {
        return ResponseEntity.ok(backlogService.addIssue(backlogId,request));
    }


    //이슈 delete
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long issueId) {
        backlogService.deleteIssue(issueId);
        return ResponseEntity.ok().build();
    }


}
