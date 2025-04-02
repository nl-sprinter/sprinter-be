package com.nl.sprinterbe.domain.contribution.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContributionDto {
    private Long userId;
    private String nickname;
    private Long contribution;


}
