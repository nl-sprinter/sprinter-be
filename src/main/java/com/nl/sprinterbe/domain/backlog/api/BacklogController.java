package com.nl.sprinterbe.domain.backlog.api;

import com.nl.sprinterbe.domain.backlog.application.BacklogService;
import com.nl.sprinterbe.domain.backlog.dto.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//Controller
// get, post > add , patch, put >> update
//      post > create

@RestController
@RequestMapping("/backlogs")
@RequiredArgsConstructor
public class BacklogController {
    private final BacklogService backlogService;
    /*
    //나의 Backlog + 나의 달성 현황
    @GetMapping("/projects/{projectId}/users/{userId}")
    public ResponseEntity<Slice<BacklogInfoResponse>> getBacklogInfoList(@PathVariable Long projectId,@PathVariable Long userId, @PageableDefault(size=5) Pageable pageable) {
        return ResponseEntity.ok(backlogService.findBacklogListByProjectId(projectId,userId,pageable));
    }

    //Backlog 정보
    @GetMapping("/projects/{projectId}/{backlogId}/users")
    public ResponseEntity<BacklogDetailResponse> getBacklogDetail(@PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.findBacklogDetailById(backlogId));
    }

    //Backlog에 걸려있는 유저
    @GetMapping("/{backlogId}/users")
    public ResponseEntity<List<BacklogUserResponse>> getBacklogUser(@PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.findUserByBacklogId(backlogId));
    }

    //Backlog에 걸려있는 Task
    @GetMapping("/{backlogId}/tasks")
    public ResponseEntity<List<BacklogTaskResponse>> getBacklogTask(@PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.findTaskByBacklogId(backlogId));
    }

    //Backlog에 걸려있는 이슈
    @GetMapping("/{backlogId}/issues")
    public ResponseEntity<List<BacklogIssueResponse>> getBacklogIssue(@PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.findIssueByBacklogId(backlogId));
    }

    //Backlog 추가 (한번에)
    @PostMapping
    public ResponseEntity<BacklogPostResponse> addBacklog(@RequestBody BacklogPostRequest request, @PathVariable Long sprintId){
        return ResponseEntity.ok(backlogService.createBacklog(request,sprintId));
    }


    //유저 수정하기 기능 중 현재 백로그에는 할당되어 있지 않지만 프로젝트에는 할당된 유저
    @GetMapping
    public ResponseEntity<List<BacklogUserResponse>> getBacklogExceptUsers(@PathVariable Long projectId,@PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.findBacklogExceptUsers(projectId,backlogId));
    }

    //백로그 제목 수정
    @PatchMapping
    public ResponseEntity<BacklogTitleResponse> updateBacklogTitle(@RequestBody BacklogTitleRequest request, @PathVariable Long backlogId){
        return ResponseEntity.ok(backlogService.updateBacklogTitle(request,backlogId));
    }

     */

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
    //Sprint에는 걸려있지만 Backlog에는 걸려있지 않은 유저 조회

    //유저 Delete
//    @DeleteMapping
//    public ResponseEntity<Void> deleteUser(@PathVariable Long backlogId,@PathVariable Long userId) {
//        backlogService.deleteUser(backlogId,userId);
//        return ResponseEntity.ok().build();
//    }
//    //유저 add
//    @PostMapping
//    public ResponseEntity<BacklogUserResponse> addUser(@PathVariable Long backlogId , @PathVariable Long userId){
//        return ResponseEntity.ok(backlogService.addBacklogUser(backlogId,userId));
//    }
//
//    //업무 Delete
//    @DeleteMapping
//    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
//        backlogService.deleteTask(taskId);
//        return ResponseEntity.ok().build();
//    }
//    //업무 add
//    @PostMapping
//    public ResponseEntity<BacklogTaskResponse> addTask(@PathVariable Long backlogId ,@RequestBody BacklogTaskRequest request) {
//        return ResponseEntity.ok(backlogService.addTask(backlogId,request));
//    }

    //---------------------------------- 수정 시 하나씩 -----------------------------------

//    //이슈 수정
//    @PatchMapping()
//    public ResponseEntity<BacklogIssueResponse> updateIssue(@PathVariable Long issueId, @RequestBody BacklogIssueRequest request){
//        return ResponseEntity.ok(backlogService.updateIssue(issueId,request));
//    }
//
//    //이슈 add
//    @PostMapping()
//    public ResponseEntity<BacklogIssueResponse> addIssue(@PathVariable Long issueId,@RequestBody BacklogIssueRequest request) {
//        return ResponseEntity.ok(backlogService.addIssue(issueId,request));
//    }
//
//
//    //이슈 delete
//    @DeleteMapping()
//    public ResponseEntity<Void> deleteIssue(@PathVariable Long issueId) {
//        backlogService.deleteIssue(issueId);
//        return ResponseEntity.ok().build();
//    }


}
