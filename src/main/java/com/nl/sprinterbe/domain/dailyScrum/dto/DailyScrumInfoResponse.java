package com.nl.sprinterbe.domain.dailyScrum.dto;

import com.nl.sprinterbe.domain.dailyScrum.entity.DailyScrum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class DailyScrumInfoResponse {
    private LocalDate createdAt;
    private String title;
    private Long sprintId;
    private Long sprintOrder;

    public static DailyScrumInfoResponse of(DailyScrum dailyScrum) {
        return DailyScrumInfoResponse.builder().createdAt(dailyScrum.getCreatedAt()).title(dailyScrum.getTitle()).sprintOrder(dailyScrum.getSprint().getSprintOrder()).sprintId(dailyScrum.getSprint().getSprintId()).build();
    }
}
