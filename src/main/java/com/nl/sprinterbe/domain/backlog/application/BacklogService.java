package com.nl.sprinterbe.domain.backlog.application;


import com.nl.sprinterbe.domain.backlog.dto.BacklogDetailResponse;
import com.nl.sprinterbe.domain.backlog.dto.BacklogInfoResponse;
import com.nl.sprinterbe.domain.dailyscrum.dto.BacklogResponse;
import com.nl.sprinterbe.domain.backlog.dto.*;
import com.nl.sprinterbe.domain.task.dto.TaskCheckStatusRequest;
import com.nl.sprinterbe.domain.task.dto.TaskCheckStatusResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface BacklogService {
    Slice<BacklogInfoResponse> findBacklogListByProjectId(Long projectId, Long userId, Pageable pageable);

    BacklogDetailResponse findBacklogDetailById(Long backlogId);

    List<UserBacklogResponse> findUserByBacklogId(Long backlogId);

    List<BacklogTaskResponse> findTasksByBacklogId(Long backlogId);

    List<BacklogIssueResponse> findIssuesByBacklogId(Long backlogId);

    List<UserBacklogResponse> findBacklogExceptUsers(Long projectId, Long backlogId);

    void createBacklog(SimpleBacklogRequest request, Long sprintId);

    void updateBacklog(BacklogUpdateRequest backlogUpdateRequest, Long backlogId);

    List<UserBacklogResponse> updateBacklogUsers(Long backlogId, BacklogUserUpdateRequest request);

    List<BacklogTaskResponse> updateBacklogTasks(Long backlogId, BacklogTaskUpdateRequest request);

    UserBacklogResponse addUserInBacklog(Long backlogId, Long userId);

    void deleteUserInBacklog(Long backlogId, Long userId);

    void deleteTask(Long taskId);

    void addTaskToBacklog(Long backlogId, String content);

    BacklogIssueResponse addIssueToBacklog(Long backlogId, String content);

    void updateIssue(Long issueId, IssueRequest issueRequest);

    void deleteIssue(Long issueId);

    List<BacklogResponse> getBacklogsExcludeDailyScrum(Long sprintId, Long dailyScrumId);

    List<ProductBacklogResponse> getProductBacklogsByProjectId(Long projectId);

    List<SprintBacklogResponse> getSprintBacklogsByProjectIdAndSprintId(Long projectId, Long sprintId);

    TaskCheckStatusResponse updateTaskCheckStatus(Long taskId, TaskCheckStatusRequest request);

    void updateTaskContent(Long taskId, TaskRequest request);

    void updateTaskUser(Long taskId, Long userId);

    BacklogTaskCompleteRateResponse getBacklogTaskCompleteRate(Long backlogId);
}
