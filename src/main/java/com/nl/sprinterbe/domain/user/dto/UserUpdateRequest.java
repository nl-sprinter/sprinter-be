package com.nl.sprinterbe.domain.user.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserUpdateRequest {

    private Long userId;
    private String nickname;
    private String currentPassword;
    private String newPassword;

    public UserUpdateRequest(Long userId, String nickname, String currentPassword, String newPassword) {
        this.userId = userId;
        this.nickname = nickname;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }


}
