package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductBacklogResponse {
    private Long sprintId;
    private String sprintName;
    private Long sprintOrder;
    private Long backlogId;
    private Boolean isFinished;
    private Long weight;

    public static ProductBacklogResponse of(Backlog backlog) {
        return ProductBacklogResponse.builder()
                .backlogId(backlog.getBacklogId())
                .weight(backlog.getWeight())
                .isFinished(backlog.getIsFinished())
                .sprintName(backlog.getSprint().getSprintName())
                .sprintId(backlog.getSprint().getSprintId())
                .sprintOrder(backlog.getSprint().getSprintOrder())
                .build();
    }
}
