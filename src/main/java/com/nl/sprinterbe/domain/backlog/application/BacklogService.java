package com.nl.sprinterbe.domain.backlog.application;


import com.nl.sprinterbe.domain.backlog.dto.BacklogDetailResponse;
import com.nl.sprinterbe.domain.backlog.dto.BacklogInfoResponse;
import com.nl.sprinterbe.domain.dailyScrum.dto.BacklogResponse;
import com.nl.sprinterbe.domain.backlog.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface BacklogService {
    Slice<BacklogInfoResponse> findBacklogListByProjectId(Long projectId, Long userId,Pageable pageable );

    BacklogDetailResponse findBacklogDetailById(Long backlogId);

    List<BacklogUserResponse> findUserByBacklogId(Long backlogId);

    List<BacklogTaskResponse> findTaskByBacklogId(Long backlogId);

    List<BacklogIssueResponse> findIssueByBacklogId(Long backlogId);

    List<BacklogUserResponse> findBacklogExceptUsers(Long projectId, Long backlogId);

    void createBacklog(SimpleBacklogRequest request, Long sprintId);

    BacklogTitleResponse updateBacklogTitle(BacklogTitleRequest request,Long backlogId);

    List<BacklogUserResponse> updateBacklogUsers(Long backlogId, BacklogUserUpdateRequest request);

    List<BacklogTaskResponse> updateBacklogTasks(Long backlogId, BacklogTaskUpdateRequest request);

    BacklogUserResponse addBacklogUser(Long backlogId, Long userId);

    void deleteUser(Long backlogId,Long userId);

    void deleteTask(Long taskId);

    BacklogTaskResponse addTask(Long backlogId,BacklogTaskRequest request);

    BacklogIssueResponse addIssue(Long backlogId,BacklogIssueRequest request);

    BacklogIssueResponse updateIssue(Long issueId,BacklogIssueRequest request);

    void deleteIssue(Long issueId);

    List<BacklogResponse> getBacklogsExcludeDailyScrum(Long sprintId, Long dailyScrumId);

    List<ProductBacklogResponse> getProductBacklogsByProjectId(Long projectId);

    List<SprintBacklogResponse> getSprintBacklogsByProjectIdAndSprintId(Long projectId, Long sprintId);
}
