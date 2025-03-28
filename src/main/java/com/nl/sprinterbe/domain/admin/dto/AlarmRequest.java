package com.nl.sprinterbe.domain.admin.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AlarmRequest {
    private List<Long> userIds;
    private String message;
}
