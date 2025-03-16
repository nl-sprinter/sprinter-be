package com.nl.sprinterbe.domain.project.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarResponse {
    private int year;
    private int month;
    private int firstDayOfWeek;  // 해당 월의 1일의 요일 (1: 일요일, 2: 월요일, ..., 7: 토요일)
    private int lastDay;         // 해당 월의 마지막 날짜
    private int lastDayOfWeek;   // 해당 월의 마지막 날짜의 요일

    public static CalendarResponse of(int year, int month, int firstDayOfWeek, int lastDay, int lastDayOfWeek) {
        return CalendarResponse.builder()
                .year(year)
                .month(month)
                .firstDayOfWeek(firstDayOfWeek)
                .lastDay(lastDay)
                .lastDayOfWeek(lastDayOfWeek)
                .build();
    }
} 