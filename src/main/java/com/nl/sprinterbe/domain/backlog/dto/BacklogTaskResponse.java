package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.task.entity.Task;
import com.nl.sprinterbe.domain.user.dto.UserInfoResponse;
import com.nl.sprinterbe.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
