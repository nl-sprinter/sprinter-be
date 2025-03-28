package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductBacklogResponse {

    private Long backlogId;
    private String title;
    private Long weight;
    private Boolean isFinished;
    private Long sprintId;
    private String sprintName;
    private Long sprintOrder;

    int completeRate;

    public static ProductBacklogResponse of(Backlog backlog) {
        return ProductBacklogResponse.builder()
                .backlogId(backlog.getBacklogId())
                .title(backlog.getTitle())
                .weight(backlog.getWeight())
                .isFinished(backlog.getIsFinished())
                .sprintId(backlog.getSprint().getSprintId())
                .sprintName(backlog.getSprint().getSprintName())
                .sprintOrder(backlog.getSprint().getSprintOrder())
                .build();
    }

    public static ProductBacklogResponse of(Backlog backlog, int completeRate) {
        return ProductBacklogResponse.builder()
                .backlogId(backlog.getBacklogId())
                .title(backlog.getTitle())
                .weight(backlog.getWeight())
                .isFinished(backlog.getIsFinished())
                .sprintId(backlog.getSprint().getSprintId())
                .sprintName(backlog.getSprint().getSprintName())
                .sprintOrder(backlog.getSprint().getSprintOrder())
                .completeRate(completeRate)
                .build();
    }


}
