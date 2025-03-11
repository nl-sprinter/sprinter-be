package com.nl.sprinterbe.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoWithTeamLeaderResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String role;
    private Boolean isProjectLeader;

}
