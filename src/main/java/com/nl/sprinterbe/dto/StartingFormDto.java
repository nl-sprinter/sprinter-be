package com.nl.sprinterbe.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 스타팅폼(설문지)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartingFormDto {

    @NotBlank
    private String projectName;

    @NotBlank
    private String projectGoal;

    @NotBlank
    private String estimatedDuration;

    @NotBlank
    private String sprintCycle;

    @NotBlank
    private int teamMembers;

    @NotBlank
    private String essentialFeatures;

}
