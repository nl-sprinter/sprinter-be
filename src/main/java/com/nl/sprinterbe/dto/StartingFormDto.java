package com.nl.sprinterbe.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
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
    private List<String> projectDomain;

    @NotBlank
    private String teamMembers;

    @NotBlank
    private List<String> teamPositions; // 팀 역할을 List로 받음

    @NotBlank
    private String projectDuration;

    @NotBlank
    private String estimatedDuration;

    @NotBlank
    private String essentialFeatures;

    @NotBlank
    private String priorityQualityAspect;

    @NotBlank
    private String sprintCycle;

    @NotBlank
    private String backlogDetailLevel;

    private String preferredTechStack;

}
