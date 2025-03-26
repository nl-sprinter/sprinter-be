package com.nl.sprinterbe.domain.schedule.dao;

import com.nl.sprinterbe.domain.schedule.dto.ScheduleSearchResponse;

import java.util.List;

public interface ScheduleSearchQueryRepository {
    List<ScheduleSearchResponse> searchSchedule(String keyword, Long projectId);
}
