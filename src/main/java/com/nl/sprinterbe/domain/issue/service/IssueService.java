package com.nl.sprinterbe.domain.issue.service;

import com.nl.sprinterbe.domain.issue.dto.CreateIssueRequest;
import com.nl.sprinterbe.domain.issue.dto.IssueRepsonse;

public interface IssueService {

    IssueRepsonse createIssue(CreateIssueRequest createIssueRequest);

    IssueRepsonse deleteIssue(Long issueId);

    IssueRepsonse deleteIssue(Long issueId, CreateIssueRequest createIssueRequest);

    IssueRepsonse modifyCheckedStatus(Long issueId, CreateIssueRequest createIssueRequest);
}
