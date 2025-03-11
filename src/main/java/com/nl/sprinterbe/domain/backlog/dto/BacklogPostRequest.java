package com.nl.sprinterbe.domain.backlog.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BacklogPostRequest {
    private String title;
    private Long weight;
    private List<UserDto> users;
    private List<TaskDto> tasks;
    private IssueDto issue;


    @Getter
    public static class UserDto {
        private Long userId;
    }

    @Getter
    public static class TaskDto {
        private String content;
    }


    @Getter
    public static class IssueDto {
        private String content;
    }


}
