package com.nl.sprinterbe.domain.task.dao;

import com.nl.sprinterbe.domain.task.dto.TaskSearchResponse;

import java.util.List;

public interface TaskSearchQueryRepository {
    List<TaskSearchResponse> searchTask(String keyword, Long projectId);
}
