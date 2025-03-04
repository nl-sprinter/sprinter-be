package com.nl.sprinterbe.domain.dailyScrum.application;

import com.nl.sprinterbe.domain.dailyScrum.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DailyScrumService {

    public List<DailyScrumInfoResponse> findDailyScrumInfoBySprintId(Long sprintId);

    public List<DailyScrumUserResponse> findDailyScrumUserBySprintId(Long sprintId);

    public List<BacklogResponse> findBacklogByDailyScrumId(Long dailyScrumId);

    public DailyScrumDetailResponse findContentByDailyScrumId(Long dailyScrumId);

    public List<DailyScrumDetailResponse> findDailyScrumByDate(LocalDate startOfDay);

    public DailyScrumPostResponse createDailyScrum(DailyScrumPostRequest request,Long projectId , Long sprintId);

    public void removeBacklog(Long dailyScrumId, Long backlogId);

    public BacklogResponse addBacklogToDailyScrum(Long dailyScrumId, Long backlogId);

    public List<DailyScrumUserResponse> findUsersNotInDailyScrum(Long projectId, Long dailyScrumId);

    public DailyScrumUserResponse addUserToDailyScrum(Long dailyScrumId, Long userId);

    public void removeUserFromDailyScrum(Long dailyScrumId, Long userId);

    public DailyScrumDetailResponse updateContent(Long dailyScrumId, String content);
}
