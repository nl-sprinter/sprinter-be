package com.nl.sprinterbe.domain.sprint.application;

import com.nl.sprinterbe.domain.sprint.dto.SprintDto;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SprintService {
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;

    public void createSprint(SprintDto sprintDto,Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        Sprint sprint = Sprint.createSprint(sprintDto, project);
        sprintRepository.save(sprint);
    }

    public void updateSprint(SprintDto sprintDto,Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + sprintId));
        sprint.setSprintName(sprintDto.getSprintName());
        sprint.setStartDate(sprintDto.getStartDate());
        sprint.setEndDate(sprintDto.getEndDate());
        sprint.setSprintOrder(sprintDto.getSprintOrder());
        sprintRepository.save(sprint);
    }

    public void deleteSprint(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + sprintId));
        sprintRepository.delete(sprint);
    }

    public List<SprintDto> getSprints(Long projectId) {
        List<Sprint> sprints = sprintRepository.findByProjectProjectId(projectId);
        List<SprintDto> sprintDto = sprints.stream()
                .map(e -> new SprintDto(e.getSprintName(), e.getStartDate(), e.getEndDate(), e.getSprintOrder()))
                .toList();
        return sprintDto;
    }

}
