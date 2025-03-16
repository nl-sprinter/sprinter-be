package com.nl.sprinterbe.domain.schedule.dto;

import com.nl.sprinterbe.domain.schedule.entity.Schedule;
import com.nl.sprinterbe.domain.schedule.entity.ScheduleType;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class ScheduleListResponse {
    //SchduleType이 Sprint면 SprintId
    //SchduleType이 Schedule이면 ScheduleId
    Long id;

    //Sprint / Scheduel 제목
    String title;
    //Sprint / Scheduel 시간
    LocalDateTime startTime;

    LocalDateTime endTime;
    //Sprint / Scheduel
    ScheduleType scheduleType;

    public static ScheduleListResponse of(Schedule schedule) {
        return ScheduleListResponse.builder()
                .id(schedule.getScheduleId())
                .title(schedule.getTitle())
                .startTime(schedule.getStartDateTime())
                .endTime(schedule.getEndDateTime())
                .scheduleType(ScheduleType.PERSONAL)
                .build();
    }

    //시간처리

    public static ScheduleListResponse of(Sprint sprint) {
        return ScheduleListResponse.builder()
                .id(sprint.getSprintId())
                .title(sprint.getSprintName())
                .startTime(sprint.getStartDate().atStartOfDay())
                .endTime(sprint.getEndDate().atTime(LocalTime.MAX))
                .scheduleType(ScheduleType.SPRINT)
                .build();
    }

}
