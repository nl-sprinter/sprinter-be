package com.nl.sprinterbe.domain.backlog.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class BacklogTaskUpdateRequest {
    // 추가할 Task 정보 목록 (content 등)
    private List<TaskDto> addTasks;
    // 삭제할 Task의 식별자 목록
    private List<Long> removeTaskIds;

    @Data
    public static class TaskDto {
        private String content;
    }
}
