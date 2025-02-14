package com.nl.sprinterbe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String nickname;
    private String password;
    private String email;
    private String role;
    public UserDTO(String email, String nickname) {
        this.nickname = nickname;
        this.email = email;
    }
}
