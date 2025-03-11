package com.nl.sprinterbe.domain.sprint.application;

import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.sprint.dto.SprintRequest;
import com.nl.sprinterbe.domain.sprint.dto.SprintResponse;
import com.nl.sprinterbe.domain.sprint.dto.SprintUpdateRequest;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import com.nl.sprinterbe.global.exception.sprint.SprintDueDateNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SprintService {
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;


    public void updateSprintName(SprintUpdateRequest request, Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + sprintId));
        sprint.setSprintName(request.getSprintName());
        sprintRepository.save(sprint);
    }

    public void deleteSprint(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + sprintId));
        sprintRepository.delete(sprint);
    }

    public List<SprintResponse> getSprintsByProjectId(Long projectId) {
        return sprintRepository.findAllByProjectProjectId(projectId).stream()
                .map(SprintResponse::of)
                .toList();
    }

    public SprintResponse createSprint(SprintRequest request, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));


        Integer sprintPeriod = project.getSprintPeriod();

        // 가장 최신 스프린트의 endDate 조회
        Sprint latestSprint = sprintRepository.findLatestSprint(projectId);
        Long latestSprintOrder = latestSprint.getSprintOrder();
        LocalDate latestSprintEndDate = latestSprint.getEndDate();

        if (latestSprintEndDate == null) {
            throw new SprintDueDateNotFoundException();
        }
        LocalDate today = LocalDate.now();
        LocalDate newStartDate = (today.isAfter(latestSprintEndDate) ? today : latestSprintEndDate.plusDays(1));
        LocalDate newEndDate = newStartDate.plusDays(sprintPeriod);
        Long newSprintOrder = latestSprintOrder + 1;


        Sprint sprint = Sprint.builder()
                .sprintName(request.getSprintName())
                .sprintOrder(newSprintOrder)
                .startDate(newStartDate)
                .endDate(newEndDate)
                .project(project)
                .build();
        sprintRepository.save(sprint);

        return SprintResponse.of(sprint);
    }

}
