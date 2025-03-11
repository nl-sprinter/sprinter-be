package com.nl.sprinterbe.domain.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class ProjectResponse {

    public Long projectId;

    public String projectName;

    public LocalDateTime createdAt;

    public ProjectResponse(Long projectId, String projectName, LocalDateTime createdAt) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.createdAt = createdAt;
    }
}
