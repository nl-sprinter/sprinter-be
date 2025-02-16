package com.nl.sprinterbe.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private String field1;

    @NotBlank
    private String field2;

    @NotBlank
    private String field3;

    @NotBlank
    private String field4;

    @NotBlank
    private String field5;

    // 문항 더 추가...
}
