package com.nl.sprinterbe.domain.user.dao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String email;

    private String nickname;

    private String role;

    private String password;
}
