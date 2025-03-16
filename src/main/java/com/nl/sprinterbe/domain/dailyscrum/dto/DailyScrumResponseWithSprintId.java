package com.nl.sprinterbe.domain.dailyscrum.dto;

import com.nl.sprinterbe.domain.dailyscrum.entity.DailyScrum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class DailyScrumResponseWithSprintId {
    private Long dailyScrumId;
    private Long sprintId;
    private LocalDate createdAt;
    private int backlogCount;
    private int userCount;

    public static DailyScrumResponseWithSprintId of(DailyScrum dailyScrum) {
        return DailyScrumResponseWithSprintId.builder()
                .dailyScrumId(dailyScrum.getDailyScrumId())
                .sprintId(dailyScrum.getSprint().getSprintId())
                .createdAt(dailyScrum.getCreatedAt())
                .backlogCount(dailyScrum.getDailyScrumBacklogs().size())
                .userCount(dailyScrum.getUserDailyScrums().size())
                .build();
    }

}
