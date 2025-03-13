package com.nl.sprinterbe.domain.project.api;

import com.nl.sprinterbe.domain.backlog.application.BacklogService;
import com.nl.sprinterbe.domain.backlog.dto.*;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentUpdateContent;
import com.nl.sprinterbe.domain.backlogcomment.service.BacklogCommentService;
import com.nl.sprinterbe.domain.dailyscrum.application.DailyScrumService;
import com.nl.sprinterbe.domain.dailyscrum.dto.*;
import com.nl.sprinterbe.domain.issue.dto.IssueCheckedDto;
import com.nl.sprinterbe.domain.issue.service.IssueService;
import com.nl.sprinterbe.domain.project.dto.SprintPeriodUpdateRequest;
import com.nl.sprinterbe.domain.sprint.application.SprintService;
import com.nl.sprinterbe.domain.sprint.dto.SprintRequest;
import com.nl.sprinterbe.domain.sprint.dto.SprintResponse;
import com.nl.sprinterbe.domain.sprint.dto.SprintUpdateRequest;
import com.nl.sprinterbe.domain.task.dto.TaskCheckStatusRequest;
import com.nl.sprinterbe.domain.task.dto.TaskCheckStatusResponse;
import com.nl.sprinterbe.domain.user.dto.UserInfoResponse;
import com.nl.sprinterbe.domain.user.dto.UserInfoWithTeamLeaderResponse;
import com.nl.sprinterbe.dto.StartingDataDto;
import com.nl.sprinterbe.domain.project.application.ProjectService;
import com.nl.sprinterbe.global.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
    private final JwtUtil jwtUtil;

    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Projects ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "프로젝트 생성", description = "StartingDataDto 를 받아서 프로젝트를 생성합니다.") // 프론트 연동 OK
    @PostMapping("/create")
    public ResponseEntity<String> createProject(@RequestBody StartingDataDto StartingDataDto, @RequestHeader("Authorization") String token) {
        projectService.createProject(StartingDataDto, jwtUtil.removeBearer(token));
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //프로젝트 삭제
    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다.") // 프론트 연동 OK
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId, @RequestHeader("Authorization") String token) {
        projectService.deleteProject(projectId, jwtUtil.removeBearer(token));
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
        boolean isProjectLeader = projectService.checkUserIsProjectLeader(projectId, jwtUtil.removeBearer(token));
        return ResponseEntity.status(HttpStatus.OK).body(isProjectLeader);
    }

    @Operation(summary = "프로젝트 유저 삭제", description = "프로젝트에 속한 유저를 삭제합니다.") // 프론트 연동 OK
    @DeleteMapping("/{projectId}/users")
    public ResponseEntity<Void> deleteUserInProject(@PathVariable Long projectId, @RequestHeader("Authorization") String token, @RequestParam Long targetUserId) {
        projectService.deleteUserInProject(projectId, jwtUtil.removeBearer(token), targetUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "프로젝트 이름 업데이트", description = "프로젝트 이름을 업데이트합니다.") // 프론트 연동 OK
    @PatchMapping("/{projectId}")
    public ResponseEntity<Void> updateProjectName(@PathVariable Long projectId, @RequestBody Map<String, String> newProjectNameMap) {
        projectService.updateProjectName(projectId, newProjectNameMap.get("projectName"));
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Product Backlogs ::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    @Operation(summary = "Product Backlog 조회", description = "Product Backlog 리스트를 조회합니다.") // 프론트 연동 OK
    @GetMapping("/{projectId}/productbacklogs")
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
    public ResponseEntity<List<SprintBacklogResponse>> getSprintBacklogs(@PathVariable Long projectId, @PathVariable Long sprintId) {
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
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users/{userId}")
    public ResponseEntity<Void> deleteUserInBacklog(@PathVariable Long backlogId, @PathVariable Long userId) {
        backlogService.deleteUserInBacklog(backlogId, userId);
        return ResponseEntity.ok().build();
    }




    //     /api/v1/projects/{projectId}/sprints/{sprintId}/backlogs
    //나의 Backlog + 나의 달성 현황
    // 3.4 프론트랑 연결 하고 다시 봐야할듯
    @Operation(summary = "내 Backlog 조회", description = "User에게 속한 Backlog 정보 제공")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/users/{userId}")
    public ResponseEntity<Slice<BacklogInfoResponse>> getBacklogInfoList(@PathVariable Long projectId, @PathVariable Long userId, @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(backlogService.findBacklogListByProjectId(projectId, userId, pageable));
    }


    //Backlog 정보
    @Operation(summary = "Backlog ", description = "backlogId의 Backlog의 제목 정보를 제공합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}")
    public ResponseEntity<BacklogDetailResponse> getBacklogDetail(@PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogService.findBacklogDetailById(backlogId));
    }


    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Task ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "Task 조회", description = "백로그에 포함된 Task 를 조회합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks")
    public ResponseEntity<List<BacklogTaskResponse>> getTasksInBacklog(@PathVariable Long backlogId) {
        return ResponseEntity.status(HttpStatus.OK).body(backlogService.findTasksByBacklogId(backlogId));
    }

    @Operation(summary = "백로그에 Task 추가", description = "백로그에 Task 를 추가합니다.")
    @PostMapping(value = "/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addTaskToBacklog(@PathVariable Long backlogId, @RequestBody TaskRequest taskRequest) {
        backlogService.addTaskToBacklog(backlogId, taskRequest.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Backlog에 Task 완료된 비율 응답 3/12
    @Operation(summary = "백로그에 Task 완료된 비율", description = "백로그에 Task 완료된 비율을 응답합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/complete-rate")
    public ResponseEntity<BacklogTaskCompleteRateResponse> getBacklogTaskCompleteRate(@PathVariable Long backlogId) {
        return new ResponseEntity<>(backlogService.getBacklogTaskCompleteRate(backlogId), HttpStatus.OK);
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
    public ResponseEntity<BacklogIssueResponse> addIssueToBacklog(@PathVariable Long backlogId, @RequestBody IssueRequest issueRequest) {
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
    /*
     * backlogComment
     * */

    //createdDate Response 수정 필요
    @Operation(summary = "댓글 생성", description = "백로그에 댓글을 생성합니다.")
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogcomments")
    public ResponseEntity<Void> createComment(
            @PathVariable Long backlogId,
            @RequestBody @Validated BacklogCommentRequest request,
            @RequestHeader("Authorization") String token) {
        backlogCommentService.createComment(backlogId, jwtUtil.removeBearer(token), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "댓글 삭제", description = "백로그에 댓글을 삭제합니다.")
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogcomments/{backlogCommentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long backlogCommentId,
            @RequestHeader("Authorization") String token) {
        backlogCommentService.deleteComment(jwtUtil.removeBearer(token), backlogCommentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /*//내가 쓴 댓글 조회기능인데 굳이 필요 없어 보이긴 해보임
    @Operation(summary = "내 댓글 조회", description = "내가 작성한 댓글을 조회합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogcomments/user")
    public ResponseEntity<List<BacklogCommentResponse>> getUserComment(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.getUserComment(jwtUtil.removeBearer(token)));
    }

    //댓글 조회  부모 자식 관계 그대로 조회가 됨
    @Operation(summary = "부모 자식 관계로 댓글 조회", description = "백로그에 달린 댓글을 조회합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogcomments")
    public ResponseEntity<List<BacklogCommentResponse>> getComments(
            @PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogCommentService.getComments(backlogId));
    }*/

    // 3/12
    @Operation(summary = "댓글 조회", description = "백로그에 달린 댓글을 조회합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogcomments")
    public ResponseEntity<List<BacklogCommentResponse>> getComments(
            @PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogCommentService.getComments(backlogId));
    }

    @Operation(summary = "댓글 내용 수정", description = "백로그에 달린 댓글을 내용을 수정합니다.")
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogcomments/{backlogCommentsId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long backlogCommentsId,
            @RequestBody @Validated BacklogCommentUpdateContent request,
            @RequestHeader("Authorization") String token) {
        backlogCommentService.updateComment(jwtUtil.removeBearer(token), backlogCommentsId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //업무 Delete
    @Operation(summary = "백로그에 달린 업무 삭제", description = "백로그에 달린 업무를 삭제합니다.")
    @DeleteMapping(("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}"))
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        backlogService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }


    //업무 check 설정/헤제  3/12
    @Operation(summary = "업무 check 설정/해제", description = "업무의 check를 설정/해제합니다.")
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}/check")
    public ResponseEntity<TaskCheckStatusResponse> updateTaskCheckStatus(@PathVariable Long taskId, @RequestBody TaskCheckStatusRequest request) {
        return new ResponseEntity<>(backlogService.updateTaskCheckStatus(taskId, request), HttpStatus.OK);
    }

    //업무 content 수정 3/12
    @Operation(summary = "업무 content 수정", description = "업무의 content를 수정합니다.")
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}/content")
    public ResponseEntity<Void> updateTaskContent(@PathVariable Long taskId, @RequestBody TaskRequest request) {
        backlogService.updateTaskContent(taskId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //업무 user 수정 3/12
    @Operation(summary = "업무 user 수정", description = "업무의 user를 수정합니다.")
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}/user")
    public ResponseEntity<Void> updateTaskUser(@PathVariable Long taskId, @RequestBody Map<String, Long> userIdMap) {
        backlogService.updateTaskUser(taskId, userIdMap.get("userId"));
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    //---------------------------------- 수정 시 하나씩 -----------------------------------




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

    @Operation(summary = "Sprint 에 DailyScrum 생성", description = "Sprint 에 DailyScrum 을 생성합니다.") // 프론트 연동 OK
    @PostMapping("/{projectId}/sprints/{sprintId}/dailyscrums")
    public ResponseEntity<Void> addDailyScrumToSprint(@PathVariable Long sprintId) {
        dailyScrumService.createDailyScrum(sprintId);
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

    @Operation(summary = "DailyScrum 의 회의노트 조회", description = "DailyScrum 의 회의노트를 불러옵니다.") // TODO
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/content")
    public ResponseEntity<Map<String, String>> getDailyScrumContent(@PathVariable Long dailyScrumId) {
        String content = dailyScrumService.findContentByDailyScrumId(dailyScrumId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("content", content));
    }

    @Operation(summary = "DailyScrum 에 회의노트 저장", description = "DailyScrum 에 회의노트를 저장합니다.") // TODO
    @PatchMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/content")
    public ResponseEntity<Void> saveDailyScrumContent(@PathVariable Long dailyScrumId, @RequestBody DailyScrumContentUpdateRequest request) {
        dailyScrumService.updateContent(dailyScrumId, request.getContent());
        return ResponseEntity.status(HttpStatus.OK).build();
    }




    @GetMapping("/{projectId}/sprints/{sprintId}/today-dailyscrums")
    //Today Scrum , 만약 TodayScrum이 2개 이상이라면 어떻게 할것인가?
    public ResponseEntity<List<DailyScrumDetailResponse>> getTodayDailyScrumDetail() {
        return ResponseEntity.ok(dailyScrumService.findDailyScrumByDate(LocalDate.now()));
    }



    //backlog 중 DailyScrum에 걸려있지 않고 Sprint에는 해당되는 백로그 조회
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/backlogs/backlog-excluded")
    public ResponseEntity<List<BacklogResponse>> getSprintBacklogsWithoutDailyScrum(@PathVariable Long sprintId, @PathVariable Long dailyScrumId) {
        return ResponseEntity.ok(backlogService.getBacklogsExcludeDailyScrum(sprintId, dailyScrumId));
    }






    //유저 중 DailyScrum에 걸려있지 않고 project에는 해당되는 유저 조회
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyscrums/{dailyScrumId}/users/dailyscrum-excluded")
    public ResponseEntity<List<DailyScrumUserResponse>> getProjectUsersNotInDailyScrum(@PathVariable Long dailyScrumId, @PathVariable Long projectId) {
        return ResponseEntity.ok(dailyScrumService.findUsersNotInDailyScrum(projectId, dailyScrumId));
    }










}
