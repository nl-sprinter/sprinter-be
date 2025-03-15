package com.nl.sprinterbe.domain.backlog.dto;

import lombok.Getter;

@Getter
public class BacklogUpdateRequest {
    private String title;
    private Long weight;
}
