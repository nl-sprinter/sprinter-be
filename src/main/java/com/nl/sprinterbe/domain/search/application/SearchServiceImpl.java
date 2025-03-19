package com.nl.sprinterbe.domain.search.application;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.dailyscrum.dao.DailyScrumRepository;
import com.nl.sprinterbe.domain.issue.dao.IssueRepository;
import com.nl.sprinterbe.domain.schedule.dao.ScheduleRepository;
import com.nl.sprinterbe.domain.search.dto.SearchResponse;
import com.nl.sprinterbe.domain.task.dao.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {

    private final BacklogRepository backlogRepository;
    private final IssueRepository issueRepository;
    private final TaskRepository taskRepository;
    private final ScheduleRepository scheduleRepository;
    private final DailyScrumRepository dailyScrumRepository;

    @Override
    public List<SearchResponse> search(String keyword) {
        List<SearchResponse> responses = new ArrayList<>();

        if(keyword == null || keyword.isEmpty() || keyword.isBlank()) {
            return responses;
        }

        makeSearchResponse(keyword, responses);
        return responses;
    }

    /*
    * db에서 검색어를 포함하는 데이터를 찾아서 responseDtos에 추가하는 메소드
    *
    * @param keyword 검색어
    * @param responseDtos 검색 결과를 담을 리스트
    * */
    private void makeSearchResponse(String keyword, List<SearchResponse> responses) {
        /*
        * db에서 한번에 꺼낼까도 생각해봤는데 너무 쿼리가 어려움
        * 또한 나중에 새로운 기능을 추가할때 어려움이 많을 거 같아
        * type별로 나눠서 검색
        * */
        backlogRepository.searchBacklog(keyword).stream().map(backlogSearchResponse ->
                SearchResponse.of(backlogSearchResponse.getType(), backlogSearchResponse.getTitle(),backlogSearchResponse.getContent(), backlogSearchResponse.getUrl())).forEach(responses::add);

        issueRepository.searchIssue(keyword).stream().map(issueSearchResponse ->
                SearchResponse.of(issueSearchResponse.getType(),issueSearchResponse.getTitle(), issueSearchResponse.getContent(), issueSearchResponse.getUrl())).forEach(responses::add);

        taskRepository.searchTask(keyword).stream().map(taskSearchResponse ->
                SearchResponse.of(taskSearchResponse.getType(), taskSearchResponse.getTitle(), taskSearchResponse.getContent(), taskSearchResponse.getUrl())).forEach(responses::add);

        scheduleRepository.searchSchedule(keyword).stream().map(scheduleSearchResponse ->
                SearchResponse.of(scheduleSearchResponse.getType(), scheduleSearchResponse.getTitle(), scheduleSearchResponse.getContent(), scheduleSearchResponse.getUrl())).forEach(responses::add);

        dailyScrumRepository.searchDailyScrum(keyword).stream().map(dailyScrumSearchResponse ->
                SearchResponse.of(dailyScrumSearchResponse.getType(), dailyScrumSearchResponse.getTitle(), dailyScrumSearchResponse.getContent(), dailyScrumSearchResponse.getUrl())).forEach(responses::add);
    }

}
