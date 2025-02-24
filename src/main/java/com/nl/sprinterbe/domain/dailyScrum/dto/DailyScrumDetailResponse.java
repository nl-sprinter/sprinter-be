package com.nl.sprinterbe.domain.dailyScrum.dto;

import com.nl.sprinterbe.domain.dailyScrum.entity.DailyScrum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DailyScrumDetailResponse {
    private Long dailyScrumId;
    private String content;
    private String title;
    private LocalDateTime startTime;

    public static DailyScrumDetailResponse of(DailyScrum dailyScrum) {
        return new DailyScrumDetailResponse(dailyScrum.getDailyScrumId(), dailyScrum.getContent(), dailyScrum.getTitle(),dailyScrum.getStartDate());
    }
}
