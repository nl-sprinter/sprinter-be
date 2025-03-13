package com.nl.sprinterbe.domain.dailyscrum.dto;

import com.nl.sprinterbe.domain.dailyscrum.entity.DailyScrum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class DailyScrumResponse {
    private Long dailyScrumId;
    private LocalDate createdAt;
    private int backlogCount;
    private int userCount;

    public static DailyScrumResponse of(DailyScrum dailyScrum) {
        return DailyScrumResponse.builder()
                .dailyScrumId(dailyScrum.getDailyScrumId())
                .createdAt(dailyScrum.getCreatedAt())
                .backlogCount(dailyScrum.getDailyScrumBacklogs().size())
                .userCount(dailyScrum.getUserDailyScrums().size())
                .build();
    }

}
