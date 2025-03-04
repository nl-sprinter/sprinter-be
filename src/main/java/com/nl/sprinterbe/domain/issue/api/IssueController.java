package com.nl.sprinterbe.domain.issue.api;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.issue.dto.CreateIssueRequest;
import com.nl.sprinterbe.domain.issue.dto.IssueRepsonse;
import com.nl.sprinterbe.domain.issue.service.IssueService;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/issue")
@Tag(name = "Issue API", description = "이슈 관련 API 입니다.")
public class IssueController {

    private final IssueService issueService;

    @Operation(summary = "이슈 생성", description = "백로그에 이슈를 생성합니다.")
    @PostMapping("create/{backlogId}")
    public ResponseEntity<IssueRepsonse> createIssue(@RequestBody @Validated CreateIssueRequest request,@PathVariable Long backlogId) {
        return new ResponseEntity<>(issueService.createIssue(request, backlogId), HttpStatus.CREATED);
    }

    @Operation(summary = "이슈 삭제", description = "이슈를 삭제합니다.")
    @DeleteMapping("delete/{issueId}")
    public ResponseEntity<IssueRepsonse> deleteIssue(@PathVariable Long issueId) {
        return new ResponseEntity<>(issueService.deleteIssue(issueId), HttpStatus.OK);
    }

    @Operation(summary = "이슈 수정", description = "이슈를 수정합니다.")
    @PatchMapping("update/{issueId}")
    public ResponseEntity<IssueRepsonse> updateIssue(@PathVariable Long issueId, @RequestBody CreateIssueRequest createIssueRequest) {
        return new ResponseEntity<>(issueService.updateIssue(issueId, createIssueRequest), HttpStatus.OK);
    }

    @Operation(summary = "이슈 전체 조회", description = "이슈를 전체를 조회합니다.")
    @GetMapping("all/{backlogId}")
    public ResponseEntity<List<IssueRepsonse>> getIssues(@PathVariable Long backlogId) {
        return new ResponseEntity<>(issueService.getIssues(backlogId), HttpStatus.OK);
    }

    @Operation(summary = "이슈 조회", description = "이슈를 조회합니다.")
    @GetMapping("{issueId}")
    public ResponseEntity<IssueRepsonse> getIssue(@PathVariable Long issueId) {
        return new ResponseEntity<>(issueService.getIssue(issueId), HttpStatus.OK);
    }

}
