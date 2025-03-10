package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.task.entity.Task;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BacklogTaskResponse {
    private String content;
    private Long taskId;

    public static BacklogTaskResponse of(Task task) {
        return BacklogTaskResponse.builder()
                .content(task.getContent())
                .taskId(task.getTaskId())
                .build();

    }
}
