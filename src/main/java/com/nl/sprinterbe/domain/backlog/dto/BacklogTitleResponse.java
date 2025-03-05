package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BacklogTitleResponse {
    private Long backlogId;
    private String title;

    public static BacklogTitleResponse of(Backlog backlog) {
        return new BacklogTitleResponse(backlog.getBacklogId(), backlog.getTitle());
    }
}
