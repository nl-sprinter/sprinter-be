package com.nl.sprinterbe.domain.project.api;

import com.nl.sprinterbe.domain.backlog.application.BacklogService;
import com.nl.sprinterbe.domain.backlog.dto.*;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogcomment.service.BacklogCommentService;
import com.nl.sprinterbe.domain.dailyscrum.application.DailyScrumService;
import com.nl.sprinterbe.domain.dailyscrum.dto.*;
import com.nl.sprinterbe.domain.issue.dto.IssueCheckedDto;
import com.nl.sprinterbe.domain.issue.service.IssueService;
import com.nl.sprinterbe.domain.notification.application.NotificationService;
import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import com.nl.sprinterbe.domain.project.dto.ProjectProgressResponse;
import com.nl.sprinterbe.domain.project.dto.SprintPeriodUpdateRequest;
import com.nl.sprinterbe.domain.schedule.application.ScheduleService;
import com.nl.sprinterbe.domain.schedule.dto.ScheduleListResponse;
import com.nl.sprinterbe.domain.schedule.dto.ScheduleDto;
import com.nl.sprinterbe.domain.schedule.dto.ScheduleResponse;
import com.nl.sprinterbe.domain.search.application.SearchService;
import com.nl.sprinterbe.domain.search.dto.SearchResponse;
import com.nl.sprinterbe.domain.sprint.application.SprintService;
import com.nl.sprinterbe.domain.sprint.dto.SprintRequest;
import com.nl.sprinterbe.domain.sprint.dto.SprintResponse;
import com.nl.sprinterbe.domain.sprint.dto.SprintUpdateRequest;
import com.nl.sprinterbe.domain.task.dto.TaskCheckedDto;
import com.nl.sprinterbe.domain.user.dto.UserInfoResponse;
import com.nl.sprinterbe.domain.user.dto.UserInfoWithTeamLeaderResponse;
import com.nl.sprinterbe.dto.StartingDataDto;
import com.nl.sprinterbe.domain.project.application.ProjectService;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
import com.nl.sprinterbe.global.security.JwtUtil;
import com.nl.sprinterbe.global.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Project API", description = "프로젝트 관련 API 입니다.")
public class ProjectController {

    private final ProjectService projectService;
    private final BacklogService backlogService;
    private final DailyScrumService dailyScrumService;
    private final SprintService sprintService;
    private final IssueService issueService;
    private final BacklogCommentService backlogCommentService;
    private final ScheduleService scheduleService;
    private final JwtUtil jwtUtil;
    private final SecurityUtil securityUtil;
    private final NotificationService notificationService;
    private final SearchService searchService;

    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Projects ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "프로젝트 생성", description = "StartingDataDto 를 받아서 프로젝트를 생성합니다.") // 프론트 연동 OK
    @PostMapping("/create")
    public ResponseEntity<String> createProject(@RequestBody StartingDataDto StartingDataDto, @RequestHeader("Authorization") String token) {
        projectService.createProject(StartingDataDto, jwtUtil.getUserIdByToken(token));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "프로젝트에 추가할 유저 검색", description = "프로젝트에 추가할 유저를 이메일 또는 닉네임으로 검색합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/users/search")
    public ResponseEntity<List<UserInfoResponse>> searchUserToAddProject(@RequestParam String keyword, @PathVariable Long projectId) {
        List<UserInfoResponse> userInfoResponse = projectService.searchUserToAddProject(keyword, projectId);
        return ResponseEntity.status(HttpStatus.OK).body(userInfoResponse);
    }

    // 프로젝트에 유저 추가
    @Operation(summary = "프로젝트 유저 추가", description = "프로젝트에 userId로 유저를 추가합니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/users")
    public ResponseEntity<Void> addUserToProject(@RequestBody Map<String, Long> userIdMap, @PathVariable Long projectId) {
        projectService.addUserToProject(userIdMap.get("userId"), projectId);
        //알림 추가
        notificationService.create(NotificationType.TEAMMATE,notificationService.makeTeammateContent(userIdMap.get("userId")),projectId,null,null);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //프로젝트 삭제
    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다.") // 프론트 연동 OK
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId, @RequestHeader("Authorization") String token) {
        projectService.deleteProject(projectId, jwtUtil.getUserIdByToken(token));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로젝트 유저 조회", description = "프로젝트에 속한 유저들을 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<UserInfoWithTeamLeaderResponse>> getUsersInProject(@PathVariable Long projectId) {
        List<UserInfoWithTeamLeaderResponse> users = projectService.getUsersInProject(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Operation(summary = "유저가 팀장인지 확인", description = "유저가 팀장인지 확인합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/users/isleader")
    public ResponseEntity<Boolean> checkUserIsProjectLeader(@PathVariable Long projectId, @RequestHeader("Authorization") String token) {
        boolean isProjectLeader = projectService.checkUserIsProjectLeader(projectId, jwtUtil.getUserIdByToken(token));
        return ResponseEntity.status(HttpStatus.OK).body(isProjectLeader);
    }

    @Operation(summary = "프로젝트 유저 삭제", description = "프로젝트에 속한 유저를 삭제합니다.") // 프론트 연동 OK
    @DeleteMapping("/{projectId}/users")
    public ResponseEntity<Void> deleteUserInProject(@PathVariable Long projectId, @RequestHeader("Authorization") String token, @RequestParam Long targetUserId) {
        projectService.deleteUserInProject(projectId, jwtUtil.getUserIdByToken(token), targetUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "프로젝트 유저 자진탈퇴", description = "프로젝트에 속한 유저가 자진탈퇴합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}/users")
    public ResponseEntity<Void> goOutUserInProject(@PathVariable Long projectId, @RequestHeader("Authorization") String token) {
        projectService.deleteUserInProject(projectId, jwtUtil.getUserIdByToken(token));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "프로젝트 이름 업데이트", description = "프로젝트 이름을 업데이트합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}")
    public ResponseEntity<Void> updateProjectName(@PathVariable Long projectId, @RequestBody Map<String, String> newProjectNameMap) {
        projectService.updateProjectName(projectId, newProjectNameMap.get("projectName"));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "프로젝트 진행률", description = "프로젝트의 백로그 진행률을 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/progress-percent")
    public ResponseEntity<ProjectProgressResponse> getProjectProgress(@PathVariable Long projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjectProgress(projectId));
    }

    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Product Backlogs ::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    @Operation(summary = "Product Backlog 조회", description = "Product Backlog 리스트를 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/productbacklogs")  // 보길 수정 3/16
    public ResponseEntity<List<ProductBacklogResponse>> getProductBacklogList(@PathVariable Long projectId) {
        return ResponseEntity.ok(backlogService.getProductBacklogsByProjectId(projectId));
    }


    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Sprints :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "스프린트 생성", description = "스프린트를 생성합니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/sprints")
    public ResponseEntity<SprintResponse> createSprint(@RequestBody SprintRequest request, @PathVariable Long projectId) {
        return ResponseEntity.ok(sprintService.createSprint(request, projectId));
    }

    @Operation(summary = "프로젝트에 속한 스프린트 조회", description = "프로젝트에 속한 스프린트들을 projectId 로 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints")
    public ResponseEntity<List<SprintResponse>> getSprintsByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(sprintService.getSprintsByProjectId(projectId));
    }

    @Operation(summary = "스프린트에 속한 백로그 조회", description = "스프린트에 속한 백로그들을 projectId와 sprintId 로 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs")
    public ResponseEntity<List<BacklogInfoResponse>> getSprintBacklogs(@PathVariable Long projectId, @PathVariable Long sprintId) {
        return ResponseEntity.status(HttpStatus.OK).body(backlogService.getSprintBacklogsByProjectIdAndSprintId(projectId, sprintId));
    }

    @Operation(summary = "스프린트 이름 수정", description = "스프린트 이름을 수정합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}/sprints/{sprintId}")
    public ResponseEntity<Void> updateSprintName(@RequestBody SprintUpdateRequest request, @PathVariable Long sprintId) {
        sprintService.updateSprintName(request, sprintId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "스프린트 주기 조회", description = "스프린트 주기를 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprintperiod")
    public ResponseEntity<Integer> getProjectsSprintPeriod(@PathVariable Long projectId) {
        int sprintPeriod = projectService.getSprintPeriod(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(sprintPeriod);
    }

    @Operation(summary = "스프린트 주기 수정", description = "스프린트 주기를 수정합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}/sprintperiod")
    public ResponseEntity<Void> updateProjectsSprintPeriod(@RequestBody SprintPeriodUpdateRequest request, @PathVariable Long projectId) {
        projectService.updateSprintPeriod(projectId, request.getSprintPeriod());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Backlogs ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "스프린트 백로그 추가", description = "스프린트에 백로그를 title 과 weight 로 추가합니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs")
    public ResponseEntity<Void> addBacklogToSprint(@RequestBody SimpleBacklogRequest request, @PathVariable Long sprintId) {
        backlogService.createBacklog(request, sprintId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "백로그 수정", description = "백로그 title 과 weight 를 수정합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}")
    public ResponseEntity<Void> updateBacklog(@RequestBody BacklogUpdateRequest backlogUpdateRequest, @PathVariable Long backlogId) {
        backlogService.updateBacklog(backlogUpdateRequest, backlogId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Backlog isFinished 토글", description = "Backlog 의 isFinished 를 토글합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/finish")
    public ResponseEntity<Map<String, Boolean>> updateBacklogFinished(@PathVariable Long backlogId, @RequestBody Map<String, Boolean> flagMap) {
        boolean resultFlag = backlogService.updateBacklogIsFinished(backlogId, flagMap.get("finish"));
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("finish", resultFlag));
    }

    @Operation(summary = "Backlog 에 소속된 유저 조회", description = "Backlog 에 소속된 유저 리스트를 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users")
    public ResponseEntity<List<UserBacklogResponse>> getUsersInBacklog(@PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogService.findUserByBacklogId(backlogId));
    }

    @Operation(summary = "Backlog 에 유저 추가", description = "Backlog 에 유저를 추가합니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users/{userId}")
    public ResponseEntity<Void> addUserInBacklog(@PathVariable Long backlogId, @PathVariable Long userId) {
        backlogService.addUserInBacklog(backlogId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Backlog 에 소속된 유저 삭제", description = "Backlog 에 소속된 유저를 삭제합니다.") // 프론트 연동 OK
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users")
    public ResponseEntity<Void> deleteUserInBacklog(@PathVariable Long backlogId, @RequestParam Long userId) {
        backlogService.deleteUserInBacklog(backlogId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 Backlog 조회", description = "User 에게 할당된 Backlog 리스트를 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints/user-backlogs") // 보길 수정 3/16
    public ResponseEntity<List<BacklogInfoResponse>> getUsersBacklogs(@PathVariable Long projectId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogService.findUserBacklogs(projectId, jwtUtil.getUserIdByToken(token)));
    }

    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Task ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "Task 조회", description = "백로그에 포함된 Task 를 조회합니다.") // 프론트 연동 OK
    @GetMapping(value = "/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BacklogTaskResponse>> getTasksInBacklog(@PathVariable Long backlogId) {
        return ResponseEntity.status(HttpStatus.OK).body(backlogService.findTasksByBacklogId(backlogId));
    }

    @Operation(summary = "백로그에 Task 추가", description = "백로그에 Task 를 추가합니다.") // 프론트 연동 OK
    @PostMapping(value = "/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks")
    public ResponseEntity<Void> addTaskToBacklog(@PathVariable Long backlogId, @RequestBody TaskRequest taskRequest) {
        backlogService.addTaskToBacklog(backlogId, taskRequest.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Task checked 토글", description = "Task 의 checked 를 토글합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}/check")
    public ResponseEntity<TaskCheckedDto> updateTaskChecked(@PathVariable Long taskId, @RequestBody TaskCheckedDto request) {
        return ResponseEntity.status(HttpStatus.OK).body(backlogService.updateTaskChecked(taskId, request.isChecked()));
    }

    @Operation(summary = "Task content 수정", description = "Task 의 content 를 수정합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}/content")
    public ResponseEntity<Void> updateTaskContent(@PathVariable Long taskId, @RequestBody TaskRequest request) {
        backlogService.updateTaskContent(taskId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Task 에 유저 추가", description = "Task 에 유저를 추가합니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}/user")
    public ResponseEntity<Void> addUserOnTask(@PathVariable Long taskId, @RequestBody Map<String, Long> userIdMap) {
        backlogService.addUserOnTask(taskId, userIdMap.get("userId"));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Task 에 유저 삭제", description = "Task 에 유저를 삭제합니다.") // 프론트 연동 OK
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}/user")
    public ResponseEntity<Void> deleteUserOnTask(@PathVariable Long taskId, @RequestParam Long userId) {
        backlogService.deleteUserOnTask(taskId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Task 삭제", description = "백로그에 포함된 Task 를 삭제합니다.") // 프론트 연동 OK
    @DeleteMapping(("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}"))
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        backlogService.deleteTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "백로그 내 Task 완료 비율", description = "백로그 내부의 Task 완료 비율을 퍼센트(정수)로 조회합니다.") // TODO
    @GetMapping(value = "/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/complete-rate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BacklogTaskCompleteRateResponse> getBacklogTaskCompleteRate(@PathVariable Long backlogId) {
        int rate = backlogService.getBacklogTaskCompleteRate(backlogId);
        return ResponseEntity.status(HttpStatus.OK).body(new BacklogTaskCompleteRateResponse(rate));
    }


    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Issue::: ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "이슈 전체 조회", description = "백로그에 포함된 이슈를 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues")
    public ResponseEntity<List<BacklogIssueResponse>> getIssuesInBacklog(@PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogService.findIssuesByBacklogId(backlogId));

    }

    @Operation(summary = "이슈 추가", description = "백로그에 이슈를 추가합니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues")
    public ResponseEntity<BacklogIssueResponse> addIssueToBacklog(@PathVariable Long projectId ,@PathVariable Long sprintId, @PathVariable Long backlogId, @RequestBody IssueRequest issueRequest) {
        String userId = securityUtil.getCurrentUserId().orElseThrow(UserNotFoundException::new);
        //알림 추가
        notificationService.create(NotificationType.ISSUE,notificationService.makeIssueContent(Long.parseLong(userId),backlogId),projectId,notificationService.makeIssueUrl(projectId,sprintId,backlogId),null);
        return ResponseEntity.ok(backlogService.addIssueToBacklog(backlogId, issueRequest.getContent()));
    }

    @Operation(summary = "이슈 checked 토글", description = "이슈의 checked 를 토글합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}/check")
    public ResponseEntity<IssueCheckedDto> updateIssueChecked(@PathVariable Long issueId, @RequestBody IssueCheckedDto issueCheckedDto) {
        return ResponseEntity.status(HttpStatus.OK).body(issueService.updateIssueChecked(issueId, issueCheckedDto.isChecked()));
    }

    @Operation(summary = "이슈 컨텐츠 수정", description = "이슈의 컨텐츠를 수정합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}")
    public ResponseEntity<Void> updateIssueContent(@PathVariable Long issueId, @RequestBody IssueRequest issueRequest) {
        backlogService.updateIssue(issueId, issueRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "이슈 삭제", description = "이슈를 삭제합니다.") // 프론트 연동 OK
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long issueId) {
        issueService.deleteIssue(issueId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }






    //---------------------------------- 수정 시 하나씩 -----------------------------------

    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: 댓글 Comment :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "댓글 조회", description = "백로그에 달린 댓글을 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogcomments")
    public ResponseEntity<List<BacklogCommentResponse>> getBacklogComments(
            @PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogCommentService.getBacklogComments(backlogId));
    }

    @Operation(summary = "댓글 생성", description = "백로그에 댓글을 생성합니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogcomments")
    public ResponseEntity<Void> createBacklogComment(
            @PathVariable Long projectId,
            @PathVariable Long sprintId,
            @PathVariable Long backlogId,
            @RequestBody @Validated BacklogCommentRequest request,
            @RequestHeader("Authorization") String token) {
        backlogCommentService.createBacklogComment(backlogId, jwtUtil.getUserIdByToken(token), request);
        String userId = securityUtil.getCurrentUserId().orElseThrow(UserNotFoundException::new);
        // 알림 추가
        notificationService.create(NotificationType.COMMENT,notificationService.makeCommentContent(Long.parseLong(userId),backlogId),projectId,notificationService.makeCommentUrl(projectId, sprintId, backlogId),null);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "댓글 삭제", description = "백로그에 댓글을 삭제합니다.") // 프론트 연동 OK
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogcomments/{backlogCommentId}")
    public ResponseEntity<Void> deleteBacklogComment(
            @PathVariable Long backlogCommentId,
            @RequestHeader("Authorization") String token) {
        backlogCommentService.deleteBacklogComment(jwtUtil.getUserIdByToken(token), backlogCommentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Like ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "좋아요 On", description = "백로그 댓글에 좋아요가 on 됩니다.")
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogcomments/{backlogCommentId}/likes")
    public ResponseEntity<Void> onLikeToBacklogComment(@PathVariable Long backlogId, @PathVariable Long backlogCommentId) {
        backlogCommentService.onLikeToBacklogComment(backlogId, backlogCommentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: DailyScrum ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "Sprint 의 DailyScrum 조회", description = "Sprint 의 DailyScrum 을 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyscrums")
    public ResponseEntity<List<DailyScrumResponse>> getDailyScrumList(@PathVariable Long sprintId) {
        return ResponseEntity.ok(dailyScrumService.findDailyScrumBySprintId(sprintId));
    }

    @Operation(summary = "오늘 날짜의 DailyScrum 조회", description = "오늘 날짜의 DailyScrum 을 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyscrums/today")
    public ResponseEntity<List<DailyScrumResponseWithSprintId>> getDailyScrumInToday(@PathVariable Long projectId) {
        return ResponseEntity.ok(dailyScrumService.findDailyScrumByDate(LocalDate.now(), projectId));
    }

    @Operation(summary = "Sprint 에 DailyScrum 생성", description = "Sprint 에 DailyScrum 을 생성합니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/sprints/{sprintId}/dailyscrums")
    public ResponseEntity<Void> addDailyScrumToSprint(@PathVariable Long projectId, @PathVariable Long sprintId) {
        Long dailyScrumId = dailyScrumService.createDailyScrum(sprintId);
        String userId = securityUtil.getCurrentUserId().orElseThrow(UserNotFoundException::new);
        // 알림 추가
        notificationService.create(NotificationType.DAILYSCRUM,notificationService.makeDailyScrumContent(Long.parseLong(userId)),projectId,notificationService.makeDailyScrumUrl(projectId, sprintId,dailyScrumId),null);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "DailyScrum 참석 유저 조회", description = "DailyScrum 에 참석한 유저를 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/users")
    public ResponseEntity<List<DailyScrumUserResponse>> getDailyScrumUserList(@PathVariable Long dailyScrumId) {
        return ResponseEntity.ok(dailyScrumService.findDailyScrumUserBySprintId(dailyScrumId));
    }

    @Operation(summary = "DailyScrum 참석 유저 추가", description = "DailyScrum 에 유저를 userId 로 추가시킵니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/users/{userId}")
    public ResponseEntity<DailyScrumUserResponse> addUserToDailyScrum(@PathVariable Long dailyScrumId, @PathVariable Long userId) {
        return ResponseEntity.ok(dailyScrumService.addUserToDailyScrum(dailyScrumId, userId));
    }

    @Operation(summary = "DailyScrum 참석 유저 삭제", description = "DailyScrum 에 있는 유저를 userId로 삭제시킵니다.") // 프론트 연동 OK
    @DeleteMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/users/{userId}")
    public ResponseEntity<Void> deleteUserFromDailyScrum(@PathVariable Long dailyScrumId, @PathVariable Long userId) {
        dailyScrumService.removeUserFromDailyScrum(dailyScrumId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "DailyScrum 백로그 조회", description = "DailyScrum 에 있는 Backlog 리스트를 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/backlogs")
    public ResponseEntity<List<BacklogResponse>> getBacklogList(@PathVariable Long dailyScrumId) {
        return ResponseEntity.ok(dailyScrumService.findBacklogByDailyScrumId(dailyScrumId));
    }

    @Operation(summary = "DailyScrum 에 백로그 추가", description = "DailyScrum 에 백로그를 추가합니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/backlogs/{backlogId}")
    public ResponseEntity<Void> addBacklogToDailyScrum(@PathVariable Long dailyScrumId, @PathVariable Long backlogId) {
        dailyScrumService.addBacklogToDailyScrum(dailyScrumId, backlogId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "DailyScrum 에 있는 백로그 삭제", description = "DailyScrum 에 있는 백로그를 삭제합니다.") // 프론트 연동 OK
    @DeleteMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/backlogs/{backlogId}")
    public ResponseEntity<Void> removeBacklogFromDailyScrum(@PathVariable Long dailyScrumId, @PathVariable Long backlogId) {
        dailyScrumService.removeBacklogFromDailyScrum(dailyScrumId, backlogId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "DailyScrum 의 회의노트 조회", description = "DailyScrum 의 회의노트를 불러옵니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/content")
    public ResponseEntity<Map<String, String>> getDailyScrumContent(@PathVariable Long dailyScrumId) {
        String content = dailyScrumService.findContentByDailyScrumId(dailyScrumId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("content", content));
    }

    @Operation(summary = "DailyScrum 에 회의노트 저장", description = "DailyScrum 에 회의노트를 저장합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/content")
    public ResponseEntity<Void> saveDailyScrumContent(@PathVariable Long dailyScrumId, @RequestBody DailyScrumContentUpdateRequest request) {
        dailyScrumService.updateContent(dailyScrumId, request.getContent());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "DailyScrum 삭제", description = "DailyScrum 을 삭제합니다") // 프론트 연동 OK
    @DeleteMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}")
    public ResponseEntity<Void> deleteDailyScrum(@PathVariable Long dailyScrumId) {
        dailyScrumService.deleteDailyScrum(dailyScrumId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Schedule ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "프로젝트 내 특정 년/월 Sprint + Schedule list 조회", description = "프로젝트 내부에서 특정 년도와 월의 Sprint + Schedule 정보를 조회합니다.")
    @GetMapping("/{projectId}/schedule")
    public ResponseEntity<List<ScheduleListResponse>> getScheduleList(@PathVariable Long projectId, @RequestParam int year, @RequestParam int month) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getScheduleList(projectId, year, month));
    }

    @Operation(summary = "scheduleId 로 Schedule 조회", description = "scheduleId 로 특정 Schedule 을 조회합니다.")
    @GetMapping("/{projectId}/schedule/{scheduleId}")
    public ResponseEntity<ScheduleDto> getScheduleByScheduleId(@PathVariable Long scheduleId) {
        ScheduleDto scheduleDto = scheduleService.getSchedule(scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleDto);
    }

    @Operation(summary = "Schedule 생성", description = "Schedule 을 생성합니다.")
    @PostMapping("/{projectId}/schedule")
    public ResponseEntity<Void> addSchedule(@RequestBody ScheduleDto request, @PathVariable Long projectId) {
        Long scheduleId = scheduleService.createSchedule(request, projectId);
        //알림 추가
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Schedule 수정", description = "해당 년도와 월의 Schedule 정보를 수정합니다.")
    @PatchMapping("/{projectId}/schedule/{scheduleId}")
    public ResponseEntity<Void> updateScheduleByScheduleId(@RequestBody ScheduleDto request, @PathVariable Long scheduleId) {
        scheduleService.updateSchedule(request, scheduleId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Schedule 삭제", description = "Schedule 을 삭제합니다.")
    @DeleteMapping("/{projectId}/schedule/{scheduleId}")
    public ResponseEntity<Void> deleteScheduleByScheduleId(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Search ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */


    @Operation(summary = "검색 기능", description = "ISSUE, TASK, BACKLOG, SCHEDULE, DAILYSCRUM을 검색합니다.")
    @GetMapping("/{projectId}/search")
    public ResponseEntity<List<SearchResponse>> search(@RequestParam(required = false) String query, @PathVariable Long projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.search(query, projectId));
    }


}
