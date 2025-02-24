package com.nl.sprinterbe.domain.dailyScrum.dto;

import com.nl.sprinterbe.domain.dailyScrum.entity.DailyScrum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DailyScrumInfoResponse {
    private LocalDateTime startDate;
    private String title;

    public static DailyScrumInfoResponse of(DailyScrum dailyScrum) {
        return new DailyScrumInfoResponse(dailyScrum.getStartDate(), dailyScrum.getTitle());
    }
}
