package com.nl.sprinterbe.domain.sprint.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class SprintDto {

    private Long sprintId;

    private String sprintName;

    private Long sprintOrder;

    public SprintDto(String sprintName, Long sprintOrder) {
        this.sprintName = sprintName;
        this.sprintOrder = sprintOrder;
    }
}
