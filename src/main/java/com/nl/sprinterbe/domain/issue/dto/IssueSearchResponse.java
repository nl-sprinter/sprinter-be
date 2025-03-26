package com.nl.sprinterbe.domain.issue.dto;

import com.nl.sprinterbe.domain.search.dto.keywordResponse;
import com.nl.sprinterbe.domain.search.type.SearchType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IssueSearchResponse extends keywordResponse {
    private Long sprintId;
    private Long backlogId;
    private String content;

    @QueryProjection
    public IssueSearchResponse(String content, Long projectId, Long sprintId, Long backlogId) {
        super(projectId);
        this.sprintId = sprintId;
        this.backlogId = backlogId;
        this.content = content;
    }

    public String getUrl() {
        return generateUrl(SearchType.ISSUE, sprintId, backlogId);
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public SearchType getType() {
        return SearchType.ISSUE;
    }
}
