package com.nl.sprinterbe.domain.schedule.dto;

import com.nl.sprinterbe.domain.schedule.entity.ScheduleColor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ScheduleDto {

    List<Long> userId;

    String title;

    Boolean isAllDay;
    LocalDateTime startTime;
    LocalDateTime endTime;

    Boolean notify;
    Integer preNotificationHours;

    ScheduleColor color;
}
