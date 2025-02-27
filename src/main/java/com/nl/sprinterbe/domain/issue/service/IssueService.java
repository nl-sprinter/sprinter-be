package com.nl.sprinterbe.domain.issue.service;

import com.nl.sprinterbe.domain.issue.dto.CreateIssueRequest;
import com.nl.sprinterbe.domain.issue.dto.IssueRepsonse;

import java.util.List;

public interface IssueService {

    IssueRepsonse createIssue(CreateIssueRequest createIssueRequest, Long backlogId);

    IssueRepsonse deleteIssue(Long issueId);

    IssueRepsonse updateIssue(Long issueId, CreateIssueRequest createIssueRequest);

    IssueRepsonse modifyCheckedStatus(Long issueId, CreateIssueRequest createIssueRequest);

    List<IssueRepsonse> getIssues(Long backlogId);

    IssueRepsonse getIssue(Long issueId);
}
