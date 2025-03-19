package com.nl.sprinterbe.domain.search.dto;

import com.nl.sprinterbe.domain.search.type.SearchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class SearchResponse {
    protected Long projectId;

    public Long getProjectId() {
        return projectId;
    }

    public String generateUrl(SearchType type, Long sprintId, Long id) {
        switch (type) {
            case ISSUE:
            case TASK:
            case BACKLOG:
                return String.format("/projects/%d/sprints/%d/backlogs/%d", projectId, sprintId, id);
            case SCHEDULE:
                return String.format("/projects/%d/calendar/schedule/%d", projectId, id);
            case DAILYSCRUM:
                return String.format("/projects/%d/sprints/%d/dailyscrums/%d", projectId, sprintId, id);
            default:
                return "";
        }
    }
}
