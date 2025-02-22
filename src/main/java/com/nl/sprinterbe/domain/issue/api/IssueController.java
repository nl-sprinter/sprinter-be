package com.nl.sprinterbe.domain.issue.api;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.issue.dto.CreateIssueRequest;
import com.nl.sprinterbe.domain.issue.dto.IssueRepsonse;
import com.nl.sprinterbe.domain.issue.service.IssueService;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/issue")
public class IssueController {

    private final IssueService issueService;
    private final BacklogRepository backlogRepository;
    private final SprintRepository sprintRepository;

    @PostMapping("create")
    public ResponseEntity<IssueRepsonse> createIssue(@RequestBody CreateIssueRequest request) {
        return new ResponseEntity<>(issueService.createIssue(request), HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{issueId}")
    public ResponseEntity<IssueRepsonse> deleteIssue(@PathVariable Long issueId) {
        return new ResponseEntity<>(issueService.deleteIssue(issueId), HttpStatus.OK);
    }

    @PatchMapping("update/{issueId}")
    public ResponseEntity<IssueRepsonse> updateIssue(@PathVariable Long issueId, @RequestBody CreateIssueRequest createIssueRequest) {
        return new ResponseEntity<>(issueService.updateIssue(issueId, createIssueRequest), HttpStatus.OK);
    }

    @GetMapping("all/{backlogId}")
    public ResponseEntity<List<IssueRepsonse>> getIssues(@PathVariable Long backlogId) {
        return new ResponseEntity<>(issueService.getIssues(backlogId), HttpStatus.OK);
    }

    @GetMapping("{issueId}")
    public ResponseEntity<IssueRepsonse> getIssue(@PathVariable Long issueId) {
        return new ResponseEntity<>(issueService.getIssue(issueId), HttpStatus.OK);
    }

    @PostMapping("test")
    public ResponseEntity<String> test() {
        Sprint sprint = sprintRepository.findBySprintId(2L);

        Backlog backlog = Backlog.builder()
                .sprint(sprint)
                .title("test")
                .build();
        backlogRepository.save(backlog);
        return new ResponseEntity<>("test", HttpStatus.CREATED);
    }
}
