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
        // 1. 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        Integer sprintPeriod = project.getSprintPeriod();

        // 2. 최신 스프린트 조회
        Sprint latestSprint = sprintRepository.findLatestSprint(projectId);

        LocalDate newStartDate;
        LocalDate newEndDate;
        Long newSprintOrder;

        if (latestSprint == null) {
            // 2-1. 스프린트가 하나도 없는 경우: 오늘부터 시작
            newStartDate = LocalDate.now();
            newEndDate = newStartDate.plusDays(sprintPeriod);
            newSprintOrder = 1L;
        } else {
            // 2-2. 기존 스프린트가 있는 경우: 마지막 스프린트 종료일 기준
            LocalDate latestSprintEndDate = latestSprint.getEndDate();
            if (latestSprintEndDate == null) {
                throw new SprintDueDateNotFoundException();
            }

            LocalDate today = LocalDate.now();
            newStartDate = today.isAfter(latestSprintEndDate)
                    ? today
                    : latestSprintEndDate.plusDays(1);
            newEndDate = newStartDate.plusDays(sprintPeriod);
            newSprintOrder = latestSprint.getSprintOrder() + 1;
        }

        // 3. 새 스프린트 생성 및 저장
        Sprint sprint = Sprint.builder()
                .sprintName(request.getSprintName())
                .sprintOrder(newSprintOrder)
                .startDate(newStartDate)
                .endDate(newEndDate)
                .project(project)
                .build();
        sprintRepository.save(sprint);

        // 4. 응답 반환
        return SprintResponse.of(sprint);
    }

}
