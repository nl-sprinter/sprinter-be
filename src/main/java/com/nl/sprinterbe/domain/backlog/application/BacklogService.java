package com.nl.sprinterbe.domain.backlog.application;


import com.nl.sprinterbe.domain.backlog.dto.BacklogDetailResponse;
import com.nl.sprinterbe.domain.backlog.dto.BacklogInfoResponse;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import org.springframework.data.domain.Page;
import com.nl.sprinterbe.domain.backlog.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface BacklogService {
    public Slice<BacklogInfoResponse> findBacklogListByProjectId(Long projectId, Long userId,Pageable pageable );

    public BacklogDetailResponse findBacklogDetailById(Long backlogId);

    public List<BacklogUserResponse> findUserByBacklogId(Long backlogId);

    public List<BacklogTaskResponse> findTaskByBacklogId(Long backlogId);

    public List<BacklogIssueResponse> findIssueByBacklogId(Long backlogId);

    public List<BacklogUserResponse> findBacklogExceptUsers(Long projectId, Long backlogId);

    public BacklogPostResponse createBacklog(BacklogPostRequest request,Long sprintId);

    public BacklogTitleResponse updateBacklogTitle(BacklogTitleRequest request,Long backlogId);

    public List<BacklogUserResponse> updateBacklogUsers(Long backlogId, BacklogUserUpdateRequest request);

    public List<BacklogTaskResponse> updateBacklogTasks(Long backlogId, BacklogTaskUpdateRequest request);

    public BacklogUserResponse addBacklogUser(Long backlogId, Long userId);

    public void deleteUser(Long backlogId,Long userId);

    public void deleteTask(Long taskId);

    public BacklogTaskResponse addTask(Long backlogId,BacklogTaskRequest request);

    public BacklogIssueResponse addIssue(Long backlogId,BacklogIssueRequest request);

    public BacklogIssueResponse updateIssue(Long issueId,BacklogIssueRequest request);

    public void deleteIssue(Long issueId);
}
