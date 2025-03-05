package com.nl.sprinterbe.domain.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class ProjectNameDto {
    public String projectName;

    public ProjectNameDto(String projectName, LocalDateTime createdAt) {
        this.projectName = projectName;
    }
}
