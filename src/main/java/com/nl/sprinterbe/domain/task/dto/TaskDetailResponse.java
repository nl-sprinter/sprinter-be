package com.nl.sprinterbe.domain.task.dto;

import com.nl.sprinterbe.domain.user.entity.User;
import lombok.Builder;

@Builder
public class TaskDetailResponse {
    private String content;
    private User user;
}
