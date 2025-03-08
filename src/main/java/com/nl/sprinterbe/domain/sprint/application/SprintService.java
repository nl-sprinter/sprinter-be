package com.nl.sprinterbe.domain.sprint.application;

import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.sprint.dto.SprintRequest;
import com.nl.sprinterbe.domain.sprint.dto.SprintResponse;
import com.nl.sprinterbe.domain.sprint.dto.SprintUpdateRequest;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SprintService {
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;


    public void updateSprint(SprintUpdateRequest request, Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + sprintId));
        sprint.setSprintName(request.getSprintName());
        sprint.setSprintOrder(request.getSprintOrder());
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

        Sprint sprint = Sprint.builder()
                .sprintName(request.getSprintName())
                .sprintOrder(request.getSprintOrder())
                .project(project)
                .build();
        sprintRepository.save(sprint);

        return SprintResponse.of(sprint);
    }

}
