package com.nl.sprinterbe.domain.todo.dto;

import com.nl.sprinterbe.domain.todo.entity.TodoType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponse {
    private TodoType todoType;
    private String content;
    private String url;
    private Long projectId;
    private String projectName;

    public TodoResponse(TodoType todoType, String content, Long projectId, String projectName, Long sprintId, Long backlogId) {
        this.todoType = todoType;
        this.content = content;
        this.projectId = projectId;
        this.projectName = projectName;
        this.url = String.format("/projects/%d/sprints/%d/backlogs/%d", projectId, sprintId, backlogId);
    }

    public TodoResponse(TodoType todoType, String content, Long projectId, String projectName, Long scheduleId) {
        this.todoType = todoType;
        this.content = content;
        this.projectId = projectId;
        this.projectName = projectName;
        this.url = String.format("/projects/%d/calendar/schedule/%d", projectId, scheduleId);
    }

}
