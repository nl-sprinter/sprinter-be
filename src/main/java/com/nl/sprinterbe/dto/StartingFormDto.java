package com.nl.sprinterbe.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 스타팅폼(설문지)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartingFormDto {

    @NotBlank
    private String projectName;

    @NotBlank
    private String projectGoal;

    @NotBlank
    private String domain;

    @NotBlank
    private Integer teamMembers;

    @NotBlank
    private List<String> teamRoles; // 팀 역할을 List로 받음

    @NotBlank
    private Integer projectDuration;

    @NotBlank
    private String essentialFeatures;

    @NotBlank
    private String priorityQualityAspect;

    @NotBlank
    private Integer sprintCycle;

    @NotBlank
    private String backlogDetailLevel;

    private String preferredTechStack;

    private String otherConsiderations;

}
