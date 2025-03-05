package com.nl.sprinterbe.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUserRequest {
    private Long userId;
    private String email;
    private String nickname;
    private String role;



}
