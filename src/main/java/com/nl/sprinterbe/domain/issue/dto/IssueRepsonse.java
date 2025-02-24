package com.nl.sprinterbe.domain.issue.dto;

import com.nl.sprinterbe.domain.issue.entity.Issue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueRepsonse {
    private Long issueId;
    private String content;
    private Boolean checked;
    private LocalDateTime createdAt;
    private Long backlogId;

    public static IssueRepsonse of(Long issueId) {
        return IssueRepsonse.builder()
                .issueId(issueId)
                .build();
    }

    public static IssueRepsonse of(Issue issue, Long backlogId) {
        return IssueRepsonse.builder()
                .issueId(issue.getIssueId())
                .content(issue.getContent())
                .checked(issue.getChecked())
                .backlogId(backlogId)
                .build();
    }

    public static IssueRepsonse of(Issue issue) {
        return IssueRepsonse.builder()
                .issueId(issue.getIssueId())
                .content(issue.getContent())
                .checked(issue.getChecked())
                .build();
    }
}
