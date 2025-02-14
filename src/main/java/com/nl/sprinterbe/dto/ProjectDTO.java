package com.nl.sprinterbe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProjectDTO {
    public String projectName;
    private Date createdAt;

    public ProjectDTO(String projectName) {
        this.projectName = projectName;
    }
}
