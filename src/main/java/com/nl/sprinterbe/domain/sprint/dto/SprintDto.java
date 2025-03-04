package com.nl.sprinterbe.domain.sprint.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class SprintDto {

    private Long sprintId;

    private String sprintName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long sprintOrder;

    public SprintDto(String sprintName, LocalDate startDate, LocalDate endDate, Long sprintOrder) {
        this.sprintName = sprintName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sprintOrder = sprintOrder;
    }
}
