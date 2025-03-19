package com.nl.sprinterbe.domain.search.application;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.dto.BacklogSearchResponse;
import com.nl.sprinterbe.domain.issue.dao.IssueRepository;
import com.nl.sprinterbe.domain.issue.dto.IssueSearchResponse;
import com.nl.sprinterbe.domain.search.dto.AllSearchResponseDto;
import com.nl.sprinterbe.domain.search.dto.SearchResponse;
import com.nl.sprinterbe.domain.search.type.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {

    private final BacklogRepository backlogRepository;
    private final IssueRepository issueRepository;

    @Override
    public List<AllSearchResponseDto> search(String keyword) {
        List<AllSearchResponseDto> responseDtos = new ArrayList<>();

        backlogRepository.searchBacklog(keyword).stream().map(backlogSearchResponse ->
                AllSearchResponseDto.of(SearchType.BACKLOG, backlogSearchResponse.getTitle(),"", backlogSearchResponse.getUrl())).forEach(responseDtos::add);

        issueRepository.searchIssue(keyword).stream().map(issueSearchResponse ->
                AllSearchResponseDto.of(SearchType.ISSUE,"", issueSearchResponse.getContent(), issueSearchResponse.getUrl())).forEach(responseDtos::add);


        return responseDtos;
    }

}
