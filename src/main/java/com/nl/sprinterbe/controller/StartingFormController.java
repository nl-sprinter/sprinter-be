package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.dto.StartingDataDto;
import com.nl.sprinterbe.dto.StartingFormDto;
import com.nl.sprinterbe.service.StartingFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/startingform")
public class StartingFormController {

    private final StartingFormService startingFormService;

    /**
     * 클라이언트로부터 설문지를 받아서 백로그를 생성해서 반환하는 컨트롤러
     * @param startingFormDto
     * @return 백로그 리스트
     */
    public ResponseEntity<StartingDataDto> getBacklogByStartingForm(@RequestBody StartingFormDto startingFormDto) {
        StartingDataDto startingDataDto = startingFormService.generateBacklog(startingFormDto);
        return ResponseEntity.status(200).body(startingDataDto);
    }

}
