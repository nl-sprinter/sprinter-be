package com.nl.sprinterbe.domain.backlog.api;

import com.nl.sprinterbe.domain.backlog.application.BacklogService;
import com.nl.sprinterbe.domain.backlog.dto.BacklogInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backlogs")
@RequiredArgsConstructor
public class BacklogController {
    private final BacklogService backlogService;

    @GetMapping("/projects/{projectId}/users/{userId}")
    public ResponseEntity<Slice<BacklogInfoResponse>> getBacklogInfoList(@PathVariable Long projectId,@PathVariable Long userId, @PageableDefault(size=5) Pageable pageable) {
        return ResponseEntity.ok(backlogService.findBacklogListByProjectId(projectId,userId,pageable));
    }


}
