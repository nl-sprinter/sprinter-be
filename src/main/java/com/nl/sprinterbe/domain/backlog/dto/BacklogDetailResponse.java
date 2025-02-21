package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.issue.dto.IssueReponse;
import com.nl.sprinterbe.domain.issue.entity.Issue;
import com.nl.sprinterbe.domain.task.dto.TaskResponse;
import com.nl.sprinterbe.domain.task.entity.Task;
import com.nl.sprinterbe.domain.user.dto.UserInfoResponse;
import com.nl.sprinterbe.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BacklogDetailResponse {
    private String title;
    private List<UserInfoResponse> users;
    private List<TaskResponse> tasks;
    private List<IssueReponse> issues;

    public static BacklogDetailResponse of(Backlog backlog, List<User> users, List<Task> tasks, List<Issue> issues  ) {
        return BacklogDetailResponse.builder()
                .title(backlog.getTitle())
                .users(users.stream().map(user->
                        UserInfoResponse.builder()
                                .userId(user.getUserId())
                                .nickname(user.getNickname())
                                .email(user.getEmail())
                                .build()
                ).toList())
                .tasks()
                .issues()

    }
}
