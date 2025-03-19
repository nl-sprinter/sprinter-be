package com.nl.sprinterbe.domain.backlog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BacklogSearchResponse {
    private Long projectId;
    private Long sprintId;
    private Long backlogId;
    private String title;

    public BacklogSearchResponse(Long backlogId, String title, Long projectId, Long sprintId) {
        this.backlogId = backlogId;
        this.title = title;
        this.projectId = projectId;
        this.sprintId = sprintId;
    }
}
