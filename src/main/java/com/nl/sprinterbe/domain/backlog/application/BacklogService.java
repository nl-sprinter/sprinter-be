package com.nl.sprinterbe.domain.backlog.application;


import com.nl.sprinterbe.domain.backlog.dto.BacklogDetailResponse;
import com.nl.sprinterbe.domain.backlog.dto.BacklogInfoResponse;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BacklogService {
    public Slice<BacklogInfoResponse> findBacklogListByProjectId(Long projectId, Long userId,Pageable pageable );

    public Slice<BacklogInfoResponse> findBacklogListBySprintId(Long sprintId, Long userId,Pageable pageable );

    public BacklogDetailResponse findBacklogDetailById(Long backlogId);
}
