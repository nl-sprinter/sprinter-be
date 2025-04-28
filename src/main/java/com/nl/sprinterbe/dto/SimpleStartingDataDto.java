package com.nl.sprinterbe.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleStartingDataDto {

    @NotEmpty
    private String projectName;

    @NotEmpty
    private int sprintPeriod;

}
