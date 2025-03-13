package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.task.entity.Task;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BacklogTaskResponse {

    private Long taskId;
    private String content;
    private Long userId;
    private Boolean checked;

    public static BacklogTaskResponse of(Task task) {
        return BacklogTaskResponse.builder()
                .taskId(task.getTaskId())
                .content(task.getContent())
                .userId(task.getUser().getUserId())
                .checked(task.getChecked())
                .build();
    }
}
