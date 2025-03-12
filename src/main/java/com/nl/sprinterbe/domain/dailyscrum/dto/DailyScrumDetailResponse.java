package com.nl.sprinterbe.domain.dailyscrum.dto;

import com.nl.sprinterbe.domain.dailyscrum.entity.DailyScrum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DailyScrumDetailResponse {
    private Long dailyScrumId;
    private String content;
    private String title;
    private LocalDate createdAt;

    public static DailyScrumDetailResponse of(DailyScrum dailyScrum) {
        return new DailyScrumDetailResponse(dailyScrum.getDailyScrumId(), dailyScrum.getContent(), dailyScrum.getTitle(),dailyScrum.getCreatedAt());
    }
}
