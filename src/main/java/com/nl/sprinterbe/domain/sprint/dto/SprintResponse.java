package com.nl.sprinterbe.domain.sprint.dto;

import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SprintResponse {

    private Long sprintId;

    private String sprintName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long sprintOrder;

    public static SprintResponse of(Sprint sprint) {
        return SprintResponse.builder()
                .sprintId(sprint.getSprintId())
                .sprintName(sprint.getSprintName())
                .sprintOrder(sprint.getSprintOrder())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .build();
    }
}
