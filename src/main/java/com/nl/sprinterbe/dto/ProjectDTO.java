package com.nl.sprinterbe.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
@Getter
@Builder
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private Date createdAt;
}
