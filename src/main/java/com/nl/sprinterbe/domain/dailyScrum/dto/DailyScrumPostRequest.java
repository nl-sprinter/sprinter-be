package com.nl.sprinterbe.domain.dailyScrum.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DailyScrumPostRequest {
    private String title;
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
