package com.nl.sprinterbe.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 프로젝트를 생성하는데 기초가 되는 데이터
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartingDataDto {

    // 프로젝트 이름
    @NotEmpty
    private String projectName;

    // 스프린트 개수
    @NotEmpty
    private Integer sprintCount;

    // 스프린트별 백로그 리스트
    // 예시) 스프린트1 -> [백로그a, 백로그b, 백로그c], 스프린트2 -> [백로그d, 백로그e], ...
    @NotEmpty
    private Map<Integer, List<String>> productBacklogListMap;


}
