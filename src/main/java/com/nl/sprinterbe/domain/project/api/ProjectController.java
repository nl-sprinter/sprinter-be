package com.nl.sprinterbe.domain.project.api;

import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogComment.service.BacklogCommentService;
import com.nl.sprinterbe.domain.issue.dto.CreateIssueRequest;
import com.nl.sprinterbe.domain.issue.dto.IssueRepsonse;
import com.nl.sprinterbe.domain.issue.service.IssueService;
import com.nl.sprinterbe.domain.project.dto.ProjectDto;
import com.nl.sprinterbe.domain.sprint.application.SprintService;
import com.nl.sprinterbe.domain.sprint.dto.SprintDto;
import com.nl.sprinterbe.dto.StartingDataDto;
import com.nl.sprinterbe.domain.project.application.ProjectService;
import com.nl.sprinterbe.domain.user.dto.UserDetailResponse;
import com.nl.sprinterbe.global.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
@Tag(name = "Project API", description = "프로젝트 관련 API 입니다.")
public class ProjectController {
    private final ProjectService projectService;
    private final JwtUtil jwtUtil;
    private final IssueService issueService;
    private final BacklogCommentService backlogCommentService;
    private final SprintService sprintService;

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
    @DeleteMapping("/delete/{projectId}")
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
    @PatchMapping("/update/{projectId}")
    public ResponseEntity<String> updateProject(@PathVariable Long projectId, @RequestBody ProjectDto projectDTO) {
        projectService.updateProject(projectId, projectDTO);
        return ResponseEntity.status(200).body("Project updated successfully");
    }
    // api/v1/project/{projectId}/
    /*
    * sprint
    * */
    //수정
    @Operation(summary = "스프린트 수정", description = "스프린트를 수정합니다.")
    @PatchMapping("/{projectId}/sprints/update")
    public ResponseEntity<String> updateSprint(@RequestBody SprintDto sprintDto) {
        sprintService.updateSprint(sprintDto);
        return ResponseEntity.status(200).body("Sprint updated successfully");
    }

    @Operation(summary = "스프린트 삭제", description = "스프린트를 삭제합니다.")
    @DeleteMapping("/{projectId}/sprints/{sprintId}/delete")
    public ResponseEntity<String> deleteSprint(@PathVariable Long sprintId) {
        sprintService.deleteSprint(sprintId);
        return ResponseEntity.status(200).body("Sprint deleted successfully");
    }

    //프로젝트의 스프린트 조회
    @Operation(summary = "프로젝트 스프린트 조회", description = "프로젝트에 속한 스프린트들을 조회합니다.")
    @GetMapping("/{projectId}/sprints")
    public ResponseEntity<?> getSprints(@PathVariable Long projectId) {
        return ResponseEntity.status(200).body(sprintService.getSprints(projectId));
    }

    /*
    * issue
    * */

    @Operation(summary = "이슈 생성", description = "백로그에 이슈를 생성합니다.")
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/create")
    public ResponseEntity<IssueRepsonse> createIssue(@RequestBody @Validated CreateIssueRequest request, @PathVariable Long backlogId) {
        return new ResponseEntity<>(issueService.createIssue(request, backlogId), HttpStatus.CREATED);
    }

    @Operation(summary = "이슈 삭제", description = "이슈를 삭제합니다.")
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}/delete")
    public ResponseEntity<IssueRepsonse> deleteIssue(@PathVariable Long issueId) {
        return new ResponseEntity<>(issueService.deleteIssue(issueId), HttpStatus.OK);
    }

    @Operation(summary = "이슈 수정", description = "이슈를 수정합니다.")
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}")
    public ResponseEntity<IssueRepsonse> updateIssue(@PathVariable Long issueId, @RequestBody CreateIssueRequest createIssueRequest) {
        return new ResponseEntity<>(issueService.updateIssue(issueId, createIssueRequest), HttpStatus.OK);
    }

    @Operation(summary = "이슈 전체 조회", description = "이슈를 전체를 조회합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues")
    public ResponseEntity<List<IssueRepsonse>> getIssues(@PathVariable Long backlogId) {
        return new ResponseEntity<>(issueService.getIssues(backlogId), HttpStatus.OK);
    }

    @Operation(summary = "이슈 조회", description = "이슈를 조회합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}")
    public ResponseEntity<IssueRepsonse> getIssue(@PathVariable Long issueId) {
        return new ResponseEntity<>(issueService.getIssue(issueId), HttpStatus.OK);
    }

    /*
    * backlogComment
    * */
    @Operation(summary = "댓글 생성", description = "백로그에 댓글을 생성합니다.")
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogComments/create")
    public ResponseEntity<BacklogCommentResponse> createComment(
            @PathVariable Long backlogId,
            @RequestBody @Validated BacklogCommentRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.createComment(backlogId,jwtUtil.removeBearer(token),request));
    }

    @Operation(summary = "댓글 수정", description = "백로그에 댓글을 수정합니다.")
    @PatchMapping("/{projectId}/sprint/{sprintId}/backlog/{backlogId}/backlogComments/{backlogCommentId}")
    public ResponseEntity<BacklogCommentResponse> updateComment(
            @PathVariable Long backlogCommentId,
            @RequestBody @Validated BacklogCommentRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.updateComment(jwtUtil.removeBearer(token), backlogCommentId,request));
    }

    @Operation(summary = "댓글 삭제", description = "백로그에 댓글을 삭제합니다.")
    @DeleteMapping("/{projectId}/sprint/{sprintId}/backlog/{backlogId}/delete/{backlogCommentId}")
    public ResponseEntity<BacklogCommentResponse> deleteComment(
            @PathVariable Long backlogCommentId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.deleteComment(jwtUtil.removeBearer(token), backlogCommentId));
    }

    @Operation(summary = "내 댓글 조회", description = "내가 작성한 댓글을 조회합니다.")
    @GetMapping("/{projectId}/sprint/{sprintId}/backlog/{backlogId}/backlogComment/user")
    public ResponseEntity<List<BacklogCommentResponse>> getUserComment(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.getUserComment(jwtUtil.removeBearer(token)));
    }

    @Operation(summary = "댓글 조회", description = "백로그에 달린 댓글을 조회합니다.")
    @GetMapping("/{projectId}/sprint/{sprintId}/backlog/{backlogId}/backlogComment")
    public ResponseEntity<List<BacklogCommentResponse>> getComments(
            @PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogCommentService.getComments(backlogId));
    }


}
