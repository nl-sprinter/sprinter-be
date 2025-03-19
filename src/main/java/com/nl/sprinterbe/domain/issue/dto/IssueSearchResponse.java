package com.nl.sprinterbe.domain.issue.dto;

import com.nl.sprinterbe.domain.search.dto.SearchResponse;
import com.nl.sprinterbe.domain.search.type.SearchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IssueSearchResponse extends SearchResponse {
    private Long sprintId;
    private Long backlogId;
    private String content;

    public IssueSearchResponse(String content, Long projectId, Long sprintId, Long backlogId) {
        super(projectId);
        this.sprintId = sprintId;
        this.backlogId = backlogId;
        this.content = content;
    }

    public String getUrl() {
        return generateUrl(SearchType.ISSUE, sprintId, backlogId);
    }
}
