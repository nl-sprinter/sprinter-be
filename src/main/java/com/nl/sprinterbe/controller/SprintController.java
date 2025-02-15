package com.nl.sprinterbe.controller;

import com.nl.sprinterbe.dto.SprintDto;
import com.nl.sprinterbe.service.SprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sprint")
public class SprintController {
    private final SprintService sprintService;
    //생성
    @PostMapping("/create/{projectId}")
    public ResponseEntity<String> createSprint(@RequestBody SprintDto sprintDto,@PathVariable Long projectId) {
        sprintService.createSprint(sprintDto, projectId);
        return ResponseEntity.status(201).body("Sprint created successfully");
    }
    //수정
    @PostMapping("/update/{sprintId}")
    public ResponseEntity<String> updateSprint(@RequestBody SprintDto sprintDto,@PathVariable Long sprintId) {
        sprintService.updateSprint(sprintDto, sprintId);
        return ResponseEntity.status(200).body("Sprint updated successfully");
    }
    @GetMapping("/delete/{sprintId}")
    public ResponseEntity<String> deleteSprint(@PathVariable Long sprintId) {
        sprintService.deleteSprint(sprintId);
        return ResponseEntity.status(200).body("Sprint deleted successfully");
    }
    //프로젝트의 스프린트 조회
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getSprints(@PathVariable Long projectId) {
        return ResponseEntity.status(200).body(sprintService.getSprints(projectId));
    }
}
