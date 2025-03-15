package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBacklogResponse {
    private Long userId;
    private String nickname;

    public static UserBacklogResponse of(User user) {
        return UserBacklogResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .build();
    }
}
