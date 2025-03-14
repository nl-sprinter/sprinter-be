package com.nl.sprinterbe.domain.dailyscrum.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DailyScrumPostRequest {
    private String title;
    private Long sprintId;
    private List<UserDto> users;
    private List<BacklogDto> backlogs;
    private String content;

    @Getter
    public static class UserDto{
        private Long userId;
    }

    @Getter
    public static class BacklogDto{
        private Long backlogId;
    }
}
