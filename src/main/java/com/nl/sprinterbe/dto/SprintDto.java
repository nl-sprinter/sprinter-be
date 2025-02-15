package com.nl.sprinterbe.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SprintDto {
    private String sprintName;

    private Date startDate;

    private Date endDate;

    private Long sprintOrder;

    public SprintDto(String sprintName, Date startDate, Date endDate, Long sprintOrder) {
        this.sprintName = sprintName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sprintOrder = sprintOrder;
    }
}
