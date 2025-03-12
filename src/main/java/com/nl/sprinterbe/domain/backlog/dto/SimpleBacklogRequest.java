package com.nl.sprinterbe.domain.backlog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleBacklogRequest {

    private String title;
    private Long weight;
}
