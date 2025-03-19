package com.nl.sprinterbe.domain.issue.dao;

import com.nl.sprinterbe.domain.issue.dto.IssueSearchResponse;

import java.util.List;

public interface IssueSearchQueryRepository {

    List<IssueSearchResponse> searchIssue(String keyword);
}
