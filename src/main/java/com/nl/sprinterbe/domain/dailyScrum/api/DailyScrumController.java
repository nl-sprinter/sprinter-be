package com.nl.sprinterbe.domain.dailyScrum.api;

import com.nl.sprinterbe.domain.dailyScrum.application.DailyScrumServiceImpl;
import com.nl.sprinterbe.domain.dailyScrum.dto.BacklogResponse;
import com.nl.sprinterbe.domain.dailyScrum.dto.DailyScrumDetailResponse;
import com.nl.sprinterbe.domain.dailyScrum.dto.DailyScrumInfoResponse;
import com.nl.sprinterbe.domain.dailyScrum.dto.DailyScrumUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DailyScrumController {
    private DailyScrumServiceImpl dailyScrumService;

    //스프린트에 걸려있는 DailyScrum 리스트
    @GetMapping
    public ResponseEntity<List<DailyScrumInfoResponse>> getDailyScrumInfoList(@PathVariable Long sprintId) {
        return ResponseEntity.ok(dailyScrumService.findDailyScrumInfoBySprintId(sprintId));
    }

    //DailyScrum의 User 리스트
    @GetMapping
    public ResponseEntity<List<DailyScrumUserResponse>> getUserInfoList(@PathVariable Long dailyScrumId) {
        return ResponseEntity.ok(dailyScrumService.findDailyScrumUserBySprintId(dailyScrumId));
    }

    //DailyScrum에 걸려있는 백로그 리스트
    @GetMapping
    public ResponseEntity<List<BacklogResponse>> getBacklogList(@PathVariable Long dailyScrumId) {
        return ResponseEntity.ok(dailyScrumService.findBacklogByDailyScrumId(dailyScrumId));
    }

    //회의 노트
    @GetMapping
    public ResponseEntity<DailyScrumDetailResponse> getDailyScrumDetail(@PathVariable Long dailyScrumId){
        return ResponseEntity.ok(dailyScrumService.findContentByDailyScrumId(dailyScrumId));
    }

    @GetMapping
    //Today Scrum , 만약 TodayScrum이 2개 이상이라면 어떻게 할것인가?
    public ResponseEntity<List<DailyScrumDetailResponse>> getTodayDailyScrumDetail(){
        return ResponseEntity.ok(dailyScrumService.findDailyScrumByDate(LocalDateTime.now()));
    }


    //DailyScrum 생성


    //DailyScrum 중 백로그 삭제

    //DailyScrum 중 백로그 추가


    //DailyScrum 유저 추가

    //DailyScrum 유저 삭제



    //DailyScrum 중 회의노트 수정





}
