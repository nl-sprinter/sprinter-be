package com.nl.sprinterbe.domain.backlog.dao;

import com.nl.sprinterbe.domain.backlog.dto.BacklogSearchResponse;

import java.util.List;

public interface BacklogSearchQueryRepository {

    List<BacklogSearchResponse> searchBacklog(String keyword, Long projectId);
}
