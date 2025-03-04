package com.nl.sprinterbe.domain.sprint.application;

import com.nl.sprinterbe.domain.sprint.dto.SprintDto;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
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


    public void updateSprint(SprintDto sprintDto,Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + sprintId));
        sprint.setSprintName(sprintDto.getSprintName());
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
                .map(e -> new SprintDto(e.getSprintName(), e.getSprintOrder()))
                .toList();
        return sprintDto;
    }

    public void createSprint(SprintDto sprintDto) {
        Sprint sprint = Sprint.builder()
                .sprintName(sprintDto.getSprintName())
                .sprintOrder(sprintDto.getSprintOrder())
                .build();
        sprintRepository.save(sprint);
    }

}
