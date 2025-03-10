package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BacklogInfoResponse {
    private Long sprintId;
    private Long sprintOrder;
    private Long backlogId;
    private String backlogTitle;
    private Boolean isFinished;



    public static BacklogInfoResponse of(Backlog backlog) {
        return BacklogInfoResponse.builder()
                .sprintId(backlog.getSprint().getSprintId())
                .sprintOrder(backlog.getSprint().getSprintOrder())
                .backlogId(backlog.getBacklogId())
                .backlogTitle(backlog.getTitle())
                .isFinished(backlog.getIsFinished())
                .build();
    }
}
