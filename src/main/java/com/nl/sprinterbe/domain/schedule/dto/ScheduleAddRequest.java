package com.nl.sprinterbe.domain.schedule.dto;

import com.nl.sprinterbe.domain.schedule.entity.ScheduleColor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScheduleAddRequest {

     List<Long> userId;

    //제목
    String title;

    //종일
    Boolean isAllDay;

    //시작
    LocalDateTime startTime;

    //종료
    LocalDateTime endTime;

    //알림여부
    Boolean isAlarmOn;

    //알림 전 시간
    Integer preNotificationTime;

    //일정색상
    ScheduleColor color;
}
