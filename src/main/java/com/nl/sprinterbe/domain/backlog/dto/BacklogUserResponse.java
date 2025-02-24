package com.nl.sprinterbe.domain.backlog.dto;

import com.nl.sprinterbe.domain.user.dto.UserInfoResponse;
import com.nl.sprinterbe.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BacklogUserResponse {
    private Long userId;
    private String nickname;
    private String email;

    public static BacklogUserResponse of(User user) {
        return BacklogUserResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }
}
