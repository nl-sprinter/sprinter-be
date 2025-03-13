package com.nl.sprinterbe.domain.dailyscrum.application;

import com.nl.sprinterbe.domain.dailyscrum.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface DailyScrumService {

    List<DailyScrumResponse> findDailyScrumBySprintId(Long sprintId);

    List<DailyScrumUserResponse> findDailyScrumUserBySprintId(Long sprintId);

    List<BacklogResponse> findBacklogByDailyScrumId(Long dailyScrumId);

    String findContentByDailyScrumId(Long dailyScrumId);

    List<DailyScrumDetailResponse> findDailyScrumByDate(LocalDate startOfDay);

    void createDailyScrum(Long sprintId);

    void removeBacklogFromDailyScrum(Long dailyScrumId, Long backlogId);

    void addBacklogToDailyScrum(Long dailyScrumId, Long backlogId);

    List<DailyScrumUserResponse> findUsersNotInDailyScrum(Long projectId, Long dailyScrumId);

    DailyScrumUserResponse addUserToDailyScrum(Long dailyScrumId, Long userId);

    void removeUserFromDailyScrum(Long dailyScrumId, Long userId);

    void updateContent(Long dailyScrumId, String content);
}
