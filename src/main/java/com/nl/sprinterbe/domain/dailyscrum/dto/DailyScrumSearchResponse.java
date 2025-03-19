package com.nl.sprinterbe.domain.dailyscrum.dto;

import com.nl.sprinterbe.domain.search.dto.keywordResponse;
import com.nl.sprinterbe.domain.search.type.SearchType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyScrumSearchResponse extends keywordResponse {
    private Long dailyScrumId;
    private Long sprintId;
    private String content;


    @QueryProjection
    public DailyScrumSearchResponse(Long projectId, Long dailyScrumId, Long sprintId, String content) {
        super(projectId);
        this.content = content;
        this.sprintId = sprintId;
        this.dailyScrumId = dailyScrumId;
    }

    public String getUrl() {
        return generateUrl(SearchType.DAILYSCRUM, sprintId, dailyScrumId);
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public SearchType getType() {
        return SearchType.DAILYSCRUM;
    }
}
