package com.nl.sprinterbe.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    private Long userId;
    private String nickname;
    private String password;
    private String email;
    private String role;
    public UserDetailResponse(String email, String nickname) {
        this.nickname = nickname;
        this.email = email;
    }
}
