package com.nl.sprinterbe.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @AllArgsConstructor
    @Data
    public static class ProjectInfo {
        @JsonProperty("project_name")
        private String projectName;
    }

    @AllArgsConstructor
    @Data
    public static class SprintInfo {
        @JsonProperty("sprint_count")
        private int sprintCount;
        @JsonProperty("sprint_duration")
        private int sprintDuration;
    }

    @AllArgsConstructor
    @Data
    public static class BacklogItem {
        @JsonProperty("sprint_number")
        private int sprintNumber;
        private String title;
        private Long weight;
    }

    
    public Map<Integer, List<BacklogItem>> getProductBacklogListMap() {
        if (backlog == null) {
            return Collections.emptyMap();
        }
        return backlog.stream()
                .collect(Collectors.groupingBy(BacklogItem::getSprintNumber));
    }


}
