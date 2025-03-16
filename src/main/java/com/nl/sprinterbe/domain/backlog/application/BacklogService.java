package com.nl.sprinterbe.domain.backlog.application;


import com.nl.sprinterbe.domain.backlog.dto.BacklogDetailResponse;
import com.nl.sprinterbe.domain.backlog.dto.BacklogInfoResponse;
import com.nl.sprinterbe.domain.dailyscrum.dto.BacklogResponse;
import com.nl.sprinterbe.domain.backlog.dto.*;
import com.nl.sprinterbe.domain.task.dto.TaskCheckedDto;

import java.util.List;

public interface BacklogService {
    List<BacklogInfoResponse> findUserBacklogs(Long projectId, Long userId);

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

    List<BacklogInfoResponse> getSprintBacklogsByProjectIdAndSprintId(Long projectId, Long sprintId);

    TaskCheckedDto updateTaskChecked(Long taskId, boolean checked);

    void updateTaskContent(Long taskId, TaskRequest request);

    void addUserOnTask(Long taskId, Long userId);

    void deleteUserOnTask(Long taskId, Long userId);

    int getBacklogTaskCompleteRate(Long backlogId);

    boolean updateBacklogIsFinished(Long backlogId, boolean finished);
}
