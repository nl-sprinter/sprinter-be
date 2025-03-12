package com.nl.sprinterbe.domain.dailyscrum.dto;

import com.nl.sprinterbe.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DailyScrumUserResponse {
    private Long userId;
    private String nickname;
    private String email;

    public static DailyScrumUserResponse of(User user) {
        return DailyScrumUserResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail()).build();
    }
}
