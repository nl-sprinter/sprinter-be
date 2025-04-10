package com.nl.sprinterbe.domain.issue.service;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.issue.dao.IssueRepository;
import com.nl.sprinterbe.domain.issue.dto.CreateIssueRequest;
import com.nl.sprinterbe.domain.issue.dto.IssueCheckedDto;
import com.nl.sprinterbe.domain.issue.dto.IssueRepsonse;
import com.nl.sprinterbe.domain.issue.entity.Issue;

import com.nl.sprinterbe.global.exception.backlog.BacklogNotFoundException;
import com.nl.sprinterbe.global.exception.issue.IssueNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final BacklogRepository backlogRepository;

    @Override
    public IssueRepsonse createIssue(CreateIssueRequest request,Long backlogId) {
        if(!backlogRepository.existsByBacklogId(backlogId)){
            throw new BacklogNotFoundException();
        }

        Issue issue = Issue.of(request);
        Long bId = relateBacklog(backlogId, issue);

        Issue savedIssue = issueRepository.save(issue);

        return IssueRepsonse.of(savedIssue, bId);
    }

    /**
     * 이슈와 백로그 단방향 매핑
     * @param backlogId 백로그 id
     * @param issue 백로그에 해당하는 issue
     */
    public Long relateBacklog(Long backlogId, Issue issue){
        Backlog backlog = backlogRepository.findById(backlogId)
                .orElseThrow(() -> new BacklogNotFoundException());

        issue.setBacklog(backlog);
        return backlog.getBacklogId();
    }


    @Override
    public void deleteIssue(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(IssueNotFoundException::new);

        issueRepository.delete(issue);
    }

    @Override
    public IssueRepsonse updateIssue(Long issueId, CreateIssueRequest createIssueRequest) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException());

        issue.modifyContent(createIssueRequest);

        return IssueRepsonse.of(issue);
    }

    @Override
    public IssueRepsonse modifyCheckedStatus(Long issueId, CreateIssueRequest createIssueRequest) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException());

        issue.modifyChecked(createIssueRequest);

        return IssueRepsonse.of(issue);
    }

    @Override
    public List<IssueRepsonse> getIssues(Long backlogId) {
        List<Issue> issues = issueRepository.findByBacklogBacklogId(backlogId);
        List<IssueRepsonse> issueRepsonses = issues.stream().map(issue -> IssueRepsonse.of(issue)).toList();
        return issueRepsonses;
    }

    @Override
    public IssueRepsonse getIssue(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException());

        return IssueRepsonse.of(issue);
    }

    @Override
    public IssueCheckedDto updateIssueChecked(Long issueId, boolean checked) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(IssueNotFoundException::new);
        checked = !checked;
        issue.setChecked(checked);
        return new IssueCheckedDto(checked);
    }


}
