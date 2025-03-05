package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.issue.entity.Issue;
import com.nl.sprinterbe.domain.task.entity.Task;
import com.nl.sprinterbe.domain.user.entity.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BacklogPostResponse {
    private Long backlogId;
    private String title;
    private Long weight;
    private List<UserDto> users;
    private List<TaskDto> tasks;
    private IssueDto issue;


    @Getter
    public static class UserDto {
        private Long userId;
        private String nickname;
        private String email;
    }

    @Getter
    public static class TaskDto {
        private String content;
        private Long taskId;
    }


    @Getter
    public static class IssueDto {
        private String content;
        private Long issueId;
    }

    public static BacklogPostResponse of(Backlog backlog, Long weight,List<User> users, List<Task> tasks , Issue issue) {
        BacklogPostResponse response = new BacklogPostResponse();

        // Backlog 정보 매핑
        response.backlogId = backlog.getBacklogId();
        response.title = backlog.getTitle();

        // User 엔티티들을 UserDto로 변환
        response.users = users.stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.userId = user.getUserId();
                    userDto.nickname = user.getNickname();
                    userDto.email = user.getEmail();
                    return userDto;
                })
                .collect(Collectors.toList());

        // Task 엔티티들을 TaskDto로 변환
        response.tasks = tasks.stream()
                .map(task -> {
                    TaskDto taskDto = new TaskDto();
                    taskDto.taskId = task.getTaskId();
                    taskDto.content = task.getContent();
                    return taskDto;
                })
                .collect(Collectors.toList());

        // Issue 엔티티를 IssueDto로 변환 (null 체크)
        if (issue != null) {
            IssueDto issueDto = new IssueDto();
            issueDto.issueId = issue.getIssueId();
            issueDto.content = issue.getContent();
            response.issue = issueDto;
        }

        response.weight = weight;

        return response;

    }
}
