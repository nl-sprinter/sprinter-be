package com.nl.sprinterbe.domain.dailyScrum.application;

import com.nl.sprinterbe.domain.dailyScrum.dto.BacklogResponse;
import com.nl.sprinterbe.domain.dailyScrum.dto.DailyScrumDetailResponse;
import com.nl.sprinterbe.domain.dailyScrum.dto.DailyScrumInfoResponse;
import com.nl.sprinterbe.domain.dailyScrum.dto.DailyScrumUserResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyScrumService {

    public List<DailyScrumInfoResponse> findDailyScrumInfoBySprintId(Long sprintId);

    public List<DailyScrumUserResponse> findDailyScrumUserBySprintId(Long sprintId);

    public List<BacklogResponse> findBacklogByDailyScrumId(Long dailyScrumId);

    public DailyScrumDetailResponse findContentByDailyScrumId(Long dailyScrumId);

    public List<DailyScrumDetailResponse> findDailyScrumByDate(LocalDateTime startOfDay);
}
