package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SprintBacklogResponse {

    private Long backlogId;
    private String title;
    private Long weight;
    private Boolean isFinished;
    private Long sprintId;
    private Long sprintOrder;

    private int completeRate;

    public static SprintBacklogResponse of(Backlog backlog) {
        return SprintBacklogResponse.builder()
                .backlogId(backlog.getBacklogId())
                .title(backlog.getTitle())
                .weight(backlog.getWeight())
                .isFinished(backlog.getIsFinished())
                .sprintId(backlog.getSprint().getSprintId())
                .sprintOrder(backlog.getSprint().getSprintOrder())
                .build();
    }
}
