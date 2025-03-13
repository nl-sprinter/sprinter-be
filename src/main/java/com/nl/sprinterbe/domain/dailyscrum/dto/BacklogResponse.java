package com.nl.sprinterbe.domain.dailyscrum.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BacklogResponse {
    private Long backlogId;
    private Boolean isFinished;
    private Long weight;
    private String title;


    public static BacklogResponse of(Backlog backlog) {
        return BacklogResponse.builder()
                .backlogId(backlog.getBacklogId())
                .isFinished(backlog.getIsFinished())
                .weight(backlog.getWeight())
                .title(backlog.getTitle())
                .build();
    }
}
