package com.nl.sprinterbe.domain.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class ProjectDto {
    public String projectName;

    public ProjectDto(String projectName, LocalDateTime createdAt) {
        this.projectName = projectName;
    }
}
