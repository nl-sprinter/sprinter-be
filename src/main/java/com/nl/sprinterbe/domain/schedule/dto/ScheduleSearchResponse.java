package com.nl.sprinterbe.domain.schedule.dto;

import com.nl.sprinterbe.domain.search.dto.keywordResponse;
import com.nl.sprinterbe.domain.search.type.SearchType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleSearchResponse extends keywordResponse {
    private Long scheduleId;
    private String title;

    @QueryProjection
    public ScheduleSearchResponse(Long projectId, Long scheduleId, String title) {
        super(projectId);
        this.title = title;
        this.scheduleId = scheduleId;
    }

    public String getUrl() {
        return generateUrl(SearchType.SCHEDULE, null, scheduleId);
    }

    @Override
    public String getContent() {
        return "";
    }

    @Override
    public SearchType getType() {
        return SearchType.SCHEDULE;
    }
}
