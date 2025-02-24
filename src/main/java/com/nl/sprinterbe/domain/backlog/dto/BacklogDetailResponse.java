package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BacklogDetailResponse {
    private Long backlogId;
    private Long sprinterId;
    private Boolean isFinished;
    private Long weight;
    private String title;


    public static BacklogDetailResponse of(Backlog backlog) {
        return BacklogDetailResponse.builder()
                .backlogId(backlog.getBacklogId())
                .sprinterId(backlog.getSprint().getSprintId())
                .isFinished(backlog.getIsFinished())
                .weight(backlog.getWeight())
                .title(backlog.getTitle())
                .build();
    }

}
