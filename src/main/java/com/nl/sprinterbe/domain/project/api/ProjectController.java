package com.nl.sprinterbe.domain.project.api;

import com.nl.sprinterbe.domain.backlog.application.BacklogService;
import com.nl.sprinterbe.domain.backlog.dto.*;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentUpdateContent;
import com.nl.sprinterbe.domain.backlogcomment.service.BacklogCommentService;
import com.nl.sprinterbe.domain.dailyScrum.application.DailyScrumService;
import com.nl.sprinterbe.domain.dailyScrum.dto.*;
import com.nl.sprinterbe.domain.issue.dto.IssueRepsonse;
import com.nl.sprinterbe.domain.issue.service.IssueService;
import com.nl.sprinterbe.domain.project.dto.SprintPeriodUpdateRequest;
import com.nl.sprinterbe.domain.sprint.application.SprintService;
import com.nl.sprinterbe.domain.sprint.dto.SprintRequest;
import com.nl.sprinterbe.domain.sprint.dto.SprintResponse;
import com.nl.sprinterbe.domain.sprint.dto.SprintUpdateRequest;
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

    //백로그 제목 수정
    @Operation(summary = "")
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/title")
    public ResponseEntity<BacklogTitleResponse> updateBacklogTitle(@RequestBody BacklogTitleRequest request, @PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogService.updateBacklogTitle(request, backlogId));
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

    //Backlog에 걸려있는 유저
    @Operation(summary = "Backlog의 걸려있는 유저", description = "Backlog의 걸려있는 유저 리스트를 제공합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users")
    public ResponseEntity<List<BacklogUserResponse>> getBacklogUser(@PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogService.findUserByBacklogId(backlogId));
    }

    //유저 수정하기 기능 중 현재 백로그에는 할당되어 있지 않지만 프로젝트에는 할당된 유저
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users-excluded")
    public ResponseEntity<List<BacklogUserResponse>> getBacklogExceptUsers(@PathVariable Long projectId, @PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogService.findBacklogExceptUsers(projectId, backlogId));
    }

    //백로그 유저 Delete
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long backlogId, @PathVariable Long userId) {
        backlogService.deleteUser(backlogId, userId);
        return ResponseEntity.ok().build();
    }

    //백로그 유저 add
    //Post 쓸 필요가 없음
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/users/{userId}")
    public ResponseEntity<BacklogUserResponse> addUser(@PathVariable Long backlogId, @PathVariable Long userId) {
        return ResponseEntity.ok(backlogService.addBacklogUser(backlogId, userId));
    }

    //Backlog에 걸려있는 Task
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks")
    public ResponseEntity<List<BacklogTaskResponse>> getBacklogTask(@PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogService.findTaskByBacklogId(backlogId));
    }


    //Backlog에 걸려있는 이슈
    /*
     * issue
     * */

/*    @Operation(summary = "이슈 생성", description = "백로그에 이슈를 생성합니다.")
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues")
    public ResponseEntity<IssueRepsonse> createIssue(@RequestBody @Validated CreateIssueRequest request, @PathVariable Long backlogId) {
        return new ResponseEntity<>(issueService.createIssue(request, backlogId), HttpStatus.CREATED);
    }*/


    @Operation(summary = "이슈 삭제", description = "이슈를 삭제합니다.")
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}")
    public ResponseEntity<IssueRepsonse> deleteIssue(@PathVariable Long issueId) {
        return new ResponseEntity<>(issueService.deleteIssue(issueId), HttpStatus.OK);
    }

/*    @Operation(summary = "이슈 수정", description = "이슈를 수정합니다.")
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}")
    public ResponseEntity<IssueRepsonse> updateIssue(@PathVariable Long issueId, @RequestBody CreateIssueRequest createIssueRequest) {
        return new ResponseEntity<>(issueService.updateIssue(issueId, createIssueRequest), HttpStatus.OK);
    }*/

    @Operation(summary = "이슈 전체 조회", description = "이슈를 전체를 조회합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues")
    public ResponseEntity<List<BacklogIssueResponse>> getBacklogIssue(@PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogService.findIssueByBacklogId(backlogId));
    }

    //Backlog 추가 (한번에)
//    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs")
    public ResponseEntity<BacklogPostResponse> addBacklog(@RequestBody BacklogPostRequest request, @PathVariable Long sprintId) {
        return ResponseEntity.ok(backlogService.createBacklog(request, sprintId));
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
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogComments")
    public ResponseEntity<BacklogCommentResponse> createComment(
            @PathVariable Long backlogId,
            @RequestBody @Validated BacklogCommentRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.createComment(backlogId, jwtUtil.removeBearer(token), request));
    }

    // 3.4 Cascade 삭제
    @Operation(summary = "댓글 삭제", description = "백로그에 댓글을 삭제합니다.")
    @DeleteMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogComments/{backlogCommentId}")
    public ResponseEntity<BacklogCommentResponse> deleteComment(
            @PathVariable Long backlogCommentId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.deleteComment(jwtUtil.removeBearer(token), backlogCommentId));
    }

    @Operation(summary = "내 댓글 조회", description = "내가 작성한 댓글을 조회합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogComments/user")
    public ResponseEntity<List<BacklogCommentResponse>> getUserComment(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.getUserComment(jwtUtil.removeBearer(token)));
    }

    @Operation(summary = "댓글 조회", description = "백로그에 달린 댓글을 조회합니다.")
    @GetMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogComments")
    public ResponseEntity<List<BacklogCommentResponse>> getComments(
            @PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogCommentService.getComments(backlogId));
    }

    @Operation(summary = "댓글 수정", description = "백로그에 댓글을 수정합니다.")
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/backlogComments/{backlogCommentsId}")
    public ResponseEntity<BacklogCommentResponse> updateComment(
            @PathVariable Long backlogCommentsId,
            @RequestBody @Validated BacklogCommentUpdateContent request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.updateComment(jwtUtil.removeBearer(token), backlogCommentsId, request));
    }

    //업무 Delete
    @DeleteMapping(("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks/{taskId}"))
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        backlogService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    //업무 add
    @PostMapping(value = "/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BacklogTaskResponse> addTask(@PathVariable Long backlogId, @RequestBody BacklogTaskRequest request) {
        return ResponseEntity.ok(backlogService.addTask(backlogId, request));
    }


    //---------------------------------- 수정 시 하나씩 -----------------------------------

    //이슈 수정
    @PatchMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues/{issueId}")
    public ResponseEntity<BacklogIssueResponse> updateIssue(@PathVariable Long issueId, @RequestBody BacklogIssueRequest request) {
        return ResponseEntity.ok(backlogService.updateIssue(issueId, request));
    }

    //이슈 add
    @PostMapping("/{projectId}/sprints/{sprintId}/backlogs/{backlogId}/issues")
    public ResponseEntity<BacklogIssueResponse> addIssue(@PathVariable Long backlogId, @RequestBody BacklogIssueRequest request) {
        return ResponseEntity.ok(backlogService.addIssue(backlogId, request));
    }

    //---------------------------------------------------------------------

    //스프린트에 걸려있는 DailyScrum 리스트
    //3.4 DailyScrum에 Sprint 관계 매핑 안되어있음
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyScrums")
    public ResponseEntity<List<DailyScrumInfoResponse>> getDailyScrumInfoList(@PathVariable Long sprintId) {
        return ResponseEntity.ok(dailyScrumService.findDailyScrumInfoBySprintId(sprintId));
    }

    //DailyScrum의 User 리스트
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyScrums/{dailyScrumId}/users")
    public ResponseEntity<List<DailyScrumUserResponse>> getUserInfoList(@PathVariable Long dailyScrumId) {
        return ResponseEntity.ok(dailyScrumService.findDailyScrumUserBySprintId(dailyScrumId));
    }

    //DailyScrum에 걸려있는 백로그 리스트
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyScrums/{dailyScrumId}/backlogs")
    public ResponseEntity<List<BacklogResponse>> getBacklogList(@PathVariable Long dailyScrumId) {
        return ResponseEntity.ok(dailyScrumService.findBacklogByDailyScrumId(dailyScrumId));
    }

    // 3.4 LocalDate 타입 때문에 다시
    @GetMapping("/{projectId}/sprints/{sprintId}/today-dailyScrums")
    //Today Scrum , 만약 TodayScrum이 2개 이상이라면 어떻게 할것인가?
    public ResponseEntity<List<DailyScrumDetailResponse>> getTodayDailyScrumDetail() {
        return ResponseEntity.ok(dailyScrumService.findDailyScrumByDate(LocalDate.now()));
    }

    //DailyScrum 생성
    @PostMapping("/{projectId}/sprints/{sprintId}/dailyScrums")
    public ResponseEntity<DailyScrumPostResponse> addDailyScrum(@RequestBody DailyScrumPostRequest request, @PathVariable Long projectId, @PathVariable Long sprintId) {
        //projectId 파라미터는 User가 project Leader인지 위해서
        return ResponseEntity.ok(dailyScrumService.createDailyScrum(request, projectId, sprintId));

    }


    //backlog 중 DailyScrum에 걸려있지 않고 Sprint에는 해당되는 백로그 조회
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyScrums/{dailyScrumId}/backlogs/backlog-excluded")
    public ResponseEntity<List<BacklogResponse>> getSprintBacklogsWithoutDailyScrum(@PathVariable Long sprintId, @PathVariable Long dailyScrumId) {
        return ResponseEntity.ok(backlogService.getBacklogsExcludeDailyScrum(sprintId, dailyScrumId));
    }


    //DailyScrum 중 백로그 삭제 , 단일 연관관계 삭제 (하나씩 )
    @DeleteMapping("/{projectId}/sprints/{sprintId}/dailyScrums/{dailyScrumId}/backlogs/{backlogId}")
    public ResponseEntity<Void> removeBacklogFromDailyScrum(
            @PathVariable Long dailyScrumId,
            @PathVariable Long backlogId) {
        dailyScrumService.removeBacklog(dailyScrumId, backlogId);
        return ResponseEntity.noContent().build();
    }

    //DailyScrum 중 백로그 추가
    @PostMapping("/{projectId}/sprints/{sprintId}/dailyScrums/{dailyScrumId}/backlogs/{backlogId}")
    public ResponseEntity<BacklogResponse> addBacklogToDailyScrum(@PathVariable Long dailyScrumId, @PathVariable Long backlogId) {
        return ResponseEntity.ok(dailyScrumService.addBacklogToDailyScrum(dailyScrumId, backlogId));
    }

    //유저 중 DailyScrum에 걸려있지 않고 project에는 해당되는 유저 조회
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyScrums/{dailyScrumId}/users/dailyScrum-excluded")
    public ResponseEntity<List<DailyScrumUserResponse>> getProjectUsersNotInDailyScrum(@PathVariable Long dailyScrumId, @PathVariable Long projectId) {
        return ResponseEntity.ok(dailyScrumService.findUsersNotInDailyScrum(projectId, dailyScrumId));
    }

    //DailyScrum 유저 추가
    @PostMapping("/{projectId}/sprints/{sprintId}/dailyScrums/{dailyScrumId}/users/{userId}")
    public ResponseEntity<DailyScrumUserResponse> addUserToDailyScrum(@PathVariable Long dailyScrumId, @PathVariable Long userId) {
        return ResponseEntity.ok(dailyScrumService.addUserToDailyScrum(dailyScrumId, userId));
    }

    //DailyScrum 유저 삭제
    @DeleteMapping("/{projectId}/sprints/{sprintId}/dailyScrums/{dailyScrumId}/users/{userId}")
    public ResponseEntity<Void> deleteUserFromDailyScrum(@PathVariable Long dailyScrumId, @PathVariable Long userId) {
        dailyScrumService.removeUserFromDailyScrum(dailyScrumId, userId);
        return ResponseEntity.noContent().build();
    }

    //회의 노트
    @GetMapping("/{projectId}/sprints/{sprintId}/dailyScrums/{dailyScrumId}/content")
    public ResponseEntity<DailyScrumDetailResponse> getDailyScrumDetail(@PathVariable Long dailyScrumId) {
        return ResponseEntity.ok(dailyScrumService.findContentByDailyScrumId(dailyScrumId));
    }

    //DailyScrum 중 회의노트 수정
    @PatchMapping("/{projectId}/sprints/{sprintId}/dailyScrums/{dailyScrumId}/content")
    public ResponseEntity<DailyScrumDetailResponse> updateDailyScrumContent(@PathVariable Long dailyScrumId, @RequestBody DailyScrumContentUpdateRequest request) {
        return ResponseEntity.ok(dailyScrumService.updateContent(dailyScrumId, request.getContent()));
    }


}
