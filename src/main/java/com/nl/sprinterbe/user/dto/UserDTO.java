package com.nl.sprinterbe.user.dto;

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
//    private String providerName;
}
