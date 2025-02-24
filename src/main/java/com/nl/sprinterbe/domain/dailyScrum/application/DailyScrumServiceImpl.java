package com.nl.sprinterbe.domain.dailyScrum.application;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.dailyScrum.dao.DailyScrumRepository;
import com.nl.sprinterbe.domain.dailyScrum.dto.BacklogResponse;
import com.nl.sprinterbe.domain.dailyScrum.dto.DailyScrumDetailResponse;
import com.nl.sprinterbe.domain.dailyScrum.dto.DailyScrumInfoResponse;
import com.nl.sprinterbe.domain.dailyScrum.dto.DailyScrumUserResponse;
import com.nl.sprinterbe.domain.dailyScrum.entity.DailyScrum;
import com.nl.sprinterbe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyScrumServiceImpl implements DailyScrumService {

    private final DailyScrumRepository dailyScrumRepository;

    @Override
    public List<DailyScrumInfoResponse> findDailyScrumInfoBySprintId(Long sprintId) {
        //List Empty 가능
        List<DailyScrum> dailyScrums = dailyScrumRepository.findDailyScrumBySprintId(sprintId);
        return dailyScrums.stream().map(DailyScrumInfoResponse::of).collect(Collectors.toList());
    }

    @Override
    public List<DailyScrumUserResponse> findDailyScrumUserBySprintId(Long sprintId) {
        List<User> users = dailyScrumRepository.findUsersByDailyScrumId(sprintId);
        return users.stream().map(DailyScrumUserResponse::of).collect(Collectors.toList());
    }

    @Override
    public List<BacklogResponse> findBacklogByDailyScrumId(Long dailyScrumId) {
        List<Backlog> backlogs = dailyScrumRepository.findBacklogsByDailyScrumId(dailyScrumId);
        return backlogs.stream().map(BacklogResponse::of).collect(Collectors.toList());
    }

    @Override
    public DailyScrumDetailResponse findContentByDailyScrumId(Long dailyScrumId) {
        DailyScrum dailyScrum = dailyScrumRepository.findById(dailyScrumId).orElseThrow(() -> new RuntimeException("Daily scrum not found"));
        return DailyScrumDetailResponse.of(dailyScrum);
    }

    //02.23) 해당 요일에 걸려있는 DailyScrum이 2개일 수 있어서 일단 List로 해놓음
    @Override
    public List<DailyScrumDetailResponse> findDailyScrumByDate(LocalDateTime startOfDay) {
        List<DailyScrum> dailyScrums = dailyScrumRepository.findByStartDate(startOfDay);
        return dailyScrums.stream().map(DailyScrumDetailResponse::of).collect(Collectors.toList());
    }
}
