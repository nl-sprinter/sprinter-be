package com.nl.sprinterbe.domain.schedule.dto;

import com.nl.sprinterbe.domain.schedule.entity.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ScheduleResponse {
    //Schedule Id
    Long scheduleId;

    //Schedule 제목
    String title;

    /**
     * Start 와 Date가 같으면 하루짜리
     */

    //Start
    LocalDate startDate;

    //End
    LocalDate endDate;

    public static ScheduleResponse of(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getScheduleId())           // 엔티티의 PK
                .title(schedule.getTitle())             // 엔티티 제목
                .startDate(schedule.getStartDateTime().toLocalDate()) // LocalDateTime -> LocalDate
                .endDate(schedule.getEndDateTime().toLocalDate())     // 동일 변환
                .build();
    }



} 