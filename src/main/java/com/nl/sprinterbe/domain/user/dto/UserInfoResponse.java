package com.nl.sprinterbe.domain.user.dto;

import com.nl.sprinterbe.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {
    private Long userId;
    private String nickname;
    private String email;
    private String role;

    public static UserInfoResponse of(User user) {
        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
