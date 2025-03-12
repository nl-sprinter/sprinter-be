package com.nl.sprinterbe.domain.dailyscrum.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.dailyscrum.entity.DailyScrum;
import com.nl.sprinterbe.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class DailyScrumPostResponse {
    private Long dailyScrumId;
    private Long sprinterId;
    private Long sprintOrder;
    private List<UserDto> users;
    private List<BacklogDto> backlogs;
    private String content;
    private LocalDate createdAt;


    public static DailyScrumPostResponse of(DailyScrum dailyScrum,List<UserDto> user, List<BacklogDto> backlogs) {
        return DailyScrumPostResponse.builder()
                .sprinterId(dailyScrum.getSprint().getSprintId())
                .sprintOrder(dailyScrum.getSprint().getSprintOrder())
                .dailyScrumId(dailyScrum.getDailyScrumId())
                .content(dailyScrum.getContent())
                .createdAt(dailyScrum.getCreatedAt())
                .users(user)
                .backlogs(backlogs).build();
    }
    @Builder
    @Getter
    public static class UserDto{
        private Long userId;
        private String nickname;
        private String email;
        private boolean isProjectLeader;

        public static UserDto of(User user,Long projectId) {
            return UserDto.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .isProjectLeader(user.isProjectLeader(projectId))
                    .build();
        }
    }

    @Builder
    @Getter
    public static class BacklogDto{
        private Long backlogId;
        private String title;

        public static BacklogDto of(Backlog backlog) {
            return BacklogDto.builder()
                    .backlogId(backlog.getBacklogId())
                    .title(backlog.getTitle()).build();
        }
    }

}
