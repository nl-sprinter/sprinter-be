package com.nl.sprinterbe.domain.dailyScrum.api;

import com.nl.sprinterbe.domain.backlog.application.BacklogServiceImpl;
import com.nl.sprinterbe.domain.backlog.dto.BacklogUserResponse;
import com.nl.sprinterbe.domain.dailyScrum.application.DailyScrumServiceImpl;
import com.nl.sprinterbe.domain.dailyScrum.dto.*;
import com.nl.sprinterbe.domain.user.entity.User;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class DailyScrumController {
    private DailyScrumServiceImpl dailyScrumService;
    private BacklogServiceImpl backlogService;
//    //스프린트에 걸려있는 DailyScrum 리스트
//    @GetMapping
//    public ResponseEntity<List<DailyScrumInfoResponse>> getDailyScrumInfoList(@PathVariable Long sprintId) {
//        return ResponseEntity.ok(dailyScrumService.findDailyScrumInfoBySprintId(sprintId));
//    }
//
//    //DailyScrum의 User 리스트
//    @GetMapping
//    public ResponseEntity<List<DailyScrumUserResponse>> getUserInfoList(@PathVariable Long dailyScrumId) {
//        return ResponseEntity.ok(dailyScrumService.findDailyScrumUserBySprintId(dailyScrumId));
//    }
//
//    //DailyScrum에 걸려있는 백로그 리스트
//    @GetMapping
//    public ResponseEntity<List<BacklogResponse>> getBacklogList(@PathVariable Long dailyScrumId) {
//        return ResponseEntity.ok(dailyScrumService.findBacklogByDailyScrumId(dailyScrumId));
//    }
//
//    //회의 노트
//    @GetMapping
//    public ResponseEntity<DailyScrumDetailResponse> getDailyScrumDetail(@PathVariable Long dailyScrumId){
//        return ResponseEntity.ok(dailyScrumService.findContentByDailyScrumId(dailyScrumId));
//    }
//
//    @GetMapping
//    //Today Scrum , 만약 TodayScrum이 2개 이상이라면 어떻게 할것인가?
//    public ResponseEntity<List<DailyScrumDetailResponse>> getTodayDailyScrumDetail(){
//        return ResponseEntity.ok(dailyScrumService.findDailyScrumByDate(LocalDateTime.now()));
//    }


    //DailyScrum 생성
    @PostMapping
    public ResponseEntity<DailyScrumPostResponse> addDailyScrum(@RequestBody DailyScrumPostRequest request, @PathVariable Long projectId) {
        //projectId 파라미터는 User가 project Leader인지 위해서
        return ResponseEntity.ok(dailyScrumService.createDailyScrum(request,projectId));

    }


    //backlog 중 DailyScrum에 걸려있지 않고 Sprint에는 해당되는 백로그 조회
    @GetMapping
    public ResponseEntity<List<BacklogResponse>> getSprintBacklogsWithoutDailyScrum(@PathVariable Long sprintId, @PathVariable Long dailScrumId){
        return ResponseEntity.ok(backlogService.getBacklogsExcludeDailyScrum(sprintId,dailScrumId));
    }


    //DailyScrum 중 백로그 삭제 , 단일 연관관계 삭제 (하나씩 )
    @DeleteMapping("/dailyScrums/{dailyScrumId}/backlogs/{backlogId}")
    public ResponseEntity<Void> removeBacklogFromDailyScrum(
            @PathVariable Long dailyScrumId,
            @PathVariable Long backlogId) {
        dailyScrumService.removeBacklog(dailyScrumId, backlogId);
        return ResponseEntity.noContent().build();
    }

    //DailyScrum 중 백로그 추가
    @PostMapping
    public ResponseEntity<BacklogResponse> addBacklogToDailyScrum(@PathVariable Long dailyScrumId,@PathVariable Long backlogId){
        return ResponseEntity.ok(dailyScrumService.addBacklogToDailyScrum(dailyScrumId, backlogId));
    }

    //유저 중 DailyScrum에 걸려있지 않고 project에는 해당되는 유저 조회
    @GetMapping("/dailyScrums/{dailyScrumId}/projects/{projectId}/users")
    public ResponseEntity<List<DailyScrumUserResponse>> getProjectUsersNotInDailyScrum(@PathVariable Long dailyScrumId, @PathVariable Long projectId) {
        return ResponseEntity.ok(dailyScrumService.findUsersNotInDailyScrum(projectId, dailyScrumId));
    }

    //DailyScrum 유저 추가
    @PostMapping("/dailyScrums/{dailyScrumId}/users")
    public ResponseEntity<DailyScrumUserResponse> addUserToDailyScrum(@PathVariable Long dailyScrumId, @RequestBody DailyScrumUserRequest request) {
        return ResponseEntity.ok(dailyScrumService.addUserToDailyScrum(dailyScrumId, request.getUserId()));
    }

    //DailyScrum 유저 삭제
    @DeleteMapping("/dailyScrums/{dailyScrumId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromDailyScrum(@PathVariable Long dailyScrumId, @RequestBody DailyScrumUserRequest request) {
        dailyScrumService.removeUserFromDailyScrum(dailyScrumId, request.getUserId());
        return ResponseEntity.noContent().build();
    }

    //DailyScrum 중 회의노트 수정
    @PutMapping("/dailyScrums/{dailyScrumId}/content")
    public ResponseEntity<DailyScrumDetailResponse> updateDailyScrumContent(@PathVariable Long dailyScrumId, @RequestBody DailyScrumContentUpdateRequest request) {
        return ResponseEntity.ok(dailyScrumService.updateContent(dailyScrumId, request.getContent()));
    }





}
