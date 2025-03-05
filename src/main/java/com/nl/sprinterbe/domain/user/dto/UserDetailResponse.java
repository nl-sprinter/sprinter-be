package com.nl.sprinterbe.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
    private Long userId;
    private String nickname;
    private String email;
    private String role;

    //3.4 Dto 종속성
    private String password;

    public UserDetailResponse(Long userId, String nickname, String email, String role) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }
}