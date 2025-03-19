package com.nl.sprinterbe.domain.task.dto;

import com.nl.sprinterbe.domain.search.dto.keywordResponse;
import com.nl.sprinterbe.domain.search.type.SearchType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskSearchResponse extends keywordResponse {
    private Long sprintId;
    private Long backlogId;
    private String content;

    @QueryProjection
    public TaskSearchResponse(String content, Long projectId, Long sprintId, Long backlogId) {
        super(projectId);
        this.backlogId = backlogId;
        this.content = content;
        this.sprintId = sprintId;
    }

    public String getUrl() {
        return generateUrl(SearchType.TASK, sprintId, backlogId);
    }


    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public SearchType getType() {
        return SearchType.TASK;
    }
}
