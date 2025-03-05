package com.nl.sprinterbe.domain.sprint.dto;

import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SprintResponse {

    private Long sprintId;

    private String sprintName;



    private Long sprintOrder;


    public static SprintResponse of(Sprint sprint) {
        return SprintResponse.builder()
                .sprintId(sprint.getSprintId())
                .sprintName(sprint.getSprintName())
                .sprintOrder(sprint.getSprintOrder())
                .build();
    }
}
