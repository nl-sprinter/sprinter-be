package com.nl.sprinterbe.domain.user.dto;

import com.nl.sprinterbe.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String role;

    public static UserInfoResponse of(User user) {
        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();
    }
}
