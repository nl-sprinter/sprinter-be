package com.nl.sprinterbe.domain.dailyscrum.application;

import com.nl.sprinterbe.domain.dailyscrum.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface DailyScrumService {

    List<DailyScrumInfoResponse> findDailyScrumInfoBySprintId(Long sprintId);

    List<DailyScrumUserResponse> findDailyScrumUserBySprintId(Long sprintId);

    List<BacklogResponse> findBacklogByDailyScrumId(Long dailyScrumId);

    DailyScrumDetailResponse findContentByDailyScrumId(Long dailyScrumId);

    List<DailyScrumDetailResponse> findDailyScrumByDate(LocalDate startOfDay);

    void createDailyScrum(Long sprintId);

    void removeBacklog(Long dailyScrumId, Long backlogId);

    BacklogResponse addBacklogToDailyScrum(Long dailyScrumId, Long backlogId);

    List<DailyScrumUserResponse> findUsersNotInDailyScrum(Long projectId, Long dailyScrumId);

    DailyScrumUserResponse addUserToDailyScrum(Long dailyScrumId, Long userId);

    void removeUserFromDailyScrum(Long dailyScrumId, Long userId);

    DailyScrumDetailResponse updateContent(Long dailyScrumId, String content);
}
