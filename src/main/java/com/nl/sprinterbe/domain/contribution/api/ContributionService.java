package com.nl.sprinterbe.domain.contribution.api;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.contribution.dao.ContributionRepository;
import com.nl.sprinterbe.domain.contribution.dto.ContributionDto;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContributionService {
    private final ContributionRepository contributionRepository;

    @Transactional(readOnly = true)
    public List<ContributionDto> getContribution(Long projectId, Long sprinterId) {
        return contributionRepository.findUserContributionBySprintAndProject(projectId, sprinterId);
    }

    @Transactional(readOnly = true)
    public List<ContributionDto> getContribution(Long projectId) {
        return contributionRepository.findUserContributionByProject(projectId);
    }
}
