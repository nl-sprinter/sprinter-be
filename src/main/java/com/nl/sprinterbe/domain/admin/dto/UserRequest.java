package com.nl.sprinterbe.domain.admin.dto;

import com.nl.sprinterbe.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRequest {
    private Long userId;
    private String nickName;
    private String password;
    private String email;
    private String roleUser;

    static public UserRequest of(User user) {
        return UserRequest.builder().userId(user.getUserId()).nickName(user.getNickname()).password("******").email(user.getEmail()).roleUser(user.getRole()).build();
    }
}
