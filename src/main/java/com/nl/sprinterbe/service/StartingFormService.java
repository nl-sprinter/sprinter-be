package com.nl.sprinterbe.service;

import com.nl.sprinterbe.dto.StartingDataDto;
import com.nl.sprinterbe.dto.StartingFormDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StartingFormService {

    /**
     * chatGPT API 로 Backlog 생성
     * 입력: 설문지 문항
     * 출력: Backlog 리스트
     */
    public StartingDataDto generateBacklog(StartingFormDto startingFormDto) {

        // chatGPT로 정보 생성하는 로직 여기에
        // 아래는 생성된 json 예시
        /**
         * {
         *      스프린트개수:3개,
         *      백로그: {
         *                  스프린트1: [백로그1, 백로그2, 백로그3],
         *                  스프린트2: [백로그4, 백로그5, 백로그6, 백로그7],
         *                  ...
         *             }
         *  }
         */
        String projectName = startingFormDto.getProjectName();
        int sprintCount = 3; // 스프린트 개수 뽑아서 여기에 할당 TODO
        Map<Integer, List<String>> productBacklogListMap = new HashMap<>(); // TODO
        // 아래는 예시로 입력되는 데이터임.
        productBacklogListMap.put(1, List.of("백로그1, 백로그2, 백로그3"));
        productBacklogListMap.put(2, List.of("백로그4, 백로그5, 백로그6", "백로그7"));
        return new StartingDataDto(projectName, sprintCount, productBacklogListMap);
    }
}
