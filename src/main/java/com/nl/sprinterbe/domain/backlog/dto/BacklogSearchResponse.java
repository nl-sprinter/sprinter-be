package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.search.dto.keywordResponse;
import com.nl.sprinterbe.domain.search.type.SearchType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BacklogSearchResponse extends keywordResponse {
    private Long sprintId;
    private Long backlogId;
    private String title;

    @QueryProjection
    public BacklogSearchResponse(Long backlogId, String title, Long projectId, Long sprintId) {
        super(projectId);
        this.backlogId = backlogId;
        this.title = title;
        this.sprintId = sprintId;
    }

    public String getUrl() {
        return generateUrl(SearchType.BACKLOG, sprintId, backlogId);
    }

    @Override
    public String getContent() {
        return "";
    }

    @Override
    public SearchType getType() {
        return SearchType.BACKLOG;
    }
}
