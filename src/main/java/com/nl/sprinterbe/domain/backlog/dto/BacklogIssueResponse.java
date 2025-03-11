package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.issue.entity.Issue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BacklogIssueResponse {
    private Long issueId;

    private String content;

    private Boolean checked;

    public static BacklogIssueResponse of(Issue issue) {
        return BacklogIssueResponse.builder()
                .issueId(issue.getIssueId())
                .content(issue.getContent())
                .checked(issue.getChecked())
                .build();
    }
}
