package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.search.dto.SearchResponse;
import com.nl.sprinterbe.domain.search.type.SearchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BacklogSearchResponse extends SearchResponse {
    private Long sprintId;
    private Long backlogId;
    private String title;

    public BacklogSearchResponse(Long backlogId, String title, Long projectId, Long sprintId) {
        super(projectId);
        this.backlogId = backlogId;
        this.title = title;
        this.sprintId = sprintId;
    }

    public String getUrl() {
        return generateUrl(SearchType.BACKLOG, sprintId, backlogId);
    }
}
