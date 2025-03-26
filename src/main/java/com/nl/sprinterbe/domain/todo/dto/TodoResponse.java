package com.nl.sprinterbe.domain.todo.dto;

import com.nl.sprinterbe.domain.todo.entity.TodoType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoResponse {
    private TodoType todoType;
    private String content;
    private String url;
    private Long projectId;
    private String projectName;

    public TodoResponse(String content, Long projectId, String projectName, Long sprintId, Long backlogId) {
        this.content = content;
        this.projectId = projectId;
        this.projectName = projectName;
        this.url = String.format("/projects/%d/sprints/%d/backlogs/%d", projectId, sprintId, backlogId);
    }

    public TodoResponse(String content, Long projectId, String projectName, Long scheduleId) {
        this.content = content;
        this.projectId = projectId;
        this.projectName = projectName;
        this.url = String.format("/projects/%d/calendar/schedule/%d", projectId, scheduleId);
    }

}
