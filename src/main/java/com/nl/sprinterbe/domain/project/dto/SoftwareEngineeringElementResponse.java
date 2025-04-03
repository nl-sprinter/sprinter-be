package com.nl.sprinterbe.domain.project.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoftwareEngineeringElementResponse {
    private Long sprintId;
    private Long sprintOrder;
    private Long estimatedBurndownPoint;
    private Long realBurndownPoint;
    private Long velocity;

    public static SoftwareEngineeringElementResponse of(Long sprintId, Long sprintOrder, Long estimatedBurndownPoint, Long realBurndownPoint, Long velocity) {
        return SoftwareEngineeringElementResponse.builder()
                .sprintId(sprintId)
                .sprintOrder(sprintOrder)
                .estimatedBurndownPoint(estimatedBurndownPoint)
                .realBurndownPoint(realBurndownPoint)
                .velocity(velocity)
                .build();
    }
}
