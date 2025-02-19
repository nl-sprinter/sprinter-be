package com.nl.sprinterbe.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 프로젝트를 생성하는데 기초가 되는 데이터
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartingDataDto {

    @NotEmpty
    private ProjectInfo project;

    @NotEmpty
    private SprintInfo sprint;

    @NotEmpty
    private List<BacklogItem> backlog;

    @Data
    public static class ProjectInfo {
        private String projectName;
    }

    @Data
    public static class SprintInfo {
        private int sprintCount;
        private int sprintDuration;
    }

    @Data
    public static class BacklogItem {
        private int sprintNumber;
        private String title;
        private int weight;
    }

}
