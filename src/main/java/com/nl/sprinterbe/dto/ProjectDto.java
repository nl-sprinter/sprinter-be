package com.nl.sprinterbe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProjectDto {
    public String projectName;
    private Date createdAt;

    public ProjectDto(String projectName) {
        this.projectName = projectName;
    }
}
