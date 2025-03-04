package com.nl.sprinterbe.domain.sprint.api;

import com.nl.sprinterbe.domain.sprint.dto.SprintDto;
import com.nl.sprinterbe.domain.sprint.application.SprintService;
import com.nl.sprinterbe.domain.user.application.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sprint")
@Tag(name = "Sprint API", description = "스프린트 관련 API 입니다.")
public class SprintController {
    private final SprintService sprintService;

    //수정
    @Operation(summary = "스프린트 수정", description = "스프린트를 수정합니다.")
    @PostMapping("/update/{sprintId}")
    public ResponseEntity<String> updateSprint(@RequestBody SprintDto sprintDto,@PathVariable Long sprintId) {
        sprintService.updateSprint(sprintDto,sprintId);
        return ResponseEntity.status(200).body("Sprint updated successfully");
    }
    @Operation(summary = "스프린트 삭제", description = "스프린트를 삭제합니다.")
    @GetMapping("/delete/{sprintId}")
    public ResponseEntity<String> deleteSprint(@PathVariable Long sprintId) {
        sprintService.deleteSprint(sprintId);
        return ResponseEntity.status(200).body("Sprint deleted successfully");
    }
    //프로젝트의 스프린트 조회
    @Operation(summary = "프로젝트 스프린트 조회", description = "프로젝트에 속한 스프린트들을 조회합니다.")
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getSprints(@PathVariable Long projectId) {
        return ResponseEntity.status(200).body(sprintService.getSprints(projectId));
    }
}
