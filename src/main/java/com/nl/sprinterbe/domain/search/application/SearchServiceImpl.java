package com.nl.sprinterbe.domain.search.application;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.dto.BacklogSearchResponse;
import com.nl.sprinterbe.domain.search.dto.SearchResponseDto;
import com.nl.sprinterbe.domain.search.type.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {

    private final BacklogRepository backlogRepository;

    @Override
    public List<SearchResponseDto> search(String keyword) {
        List<BacklogSearchResponse> backlogSearchResponses = backlogRepository.searchBacklog(keyword);

        return backlogSearchResponses.stream().map(backlogSearchResponse ->
                SearchResponseDto.of(SearchType.BACKLOG, backlogSearchResponse.getTitle(),BacklogUrlMaker(backlogSearchResponse))).toList();
    }

    private String BacklogUrlMaker(BacklogSearchResponse backlogSearchResponses) {
        return generateUrl(SearchType.BACKLOG,
                backlogSearchResponses.getProjectId(),
                backlogSearchResponses.getSprintId(),
                backlogSearchResponses.getBacklogId());
    }

    private String generateUrl(SearchType type, Long projectId, Long sprintId, Long Id) {
        switch (type) {
            case ISSUE:
            case TASK:
            case BACKLOG:
                return String.format("/projects/%d/sprints/%d/backlogs/%d", projectId, sprintId, Id);
            case SCHEDULE:
                return String.format("/projects/%d/calendar/schedule/%d", projectId, Id);
            case DAILYSCRUM:
                return String.format("/projects/%d/sprints/%d/dailyscrums/%d", projectId, sprintId, Id);
            default:
                return "";
        }
    }

}
