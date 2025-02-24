package com.nl.sprinterbe.domain.user.dao;

import lombok.Getter;

@Getter
public class SignUpResponseDto {
    private String message;

    public SignUpResponseDto(String message) {
        this.message = message;
    }
}
