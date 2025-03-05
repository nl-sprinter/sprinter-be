package com.nl.sprinterbe.domain.dailyScrum.application;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.dailyScrum.dao.DailyScrumBacklogRepository;
import com.nl.sprinterbe.domain.dailyScrum.dao.DailyScrumRepository;
import com.nl.sprinterbe.domain.dailyScrum.dao.UserDailyScrumRepository;
import com.nl.sprinterbe.domain.dailyScrum.dto.*;
import com.nl.sprinterbe.domain.dailyScrum.entity.DailyScrum;
import com.nl.sprinterbe.domain.dailyScrum.entity.DailyScrumBacklog;
import com.nl.sprinterbe.domain.dailyScrum.entity.UserDailyScrum;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.userProject.dao.UserProjectRepository;
import com.nl.sprinterbe.domain.userProject.entity.UserProject;
import com.nl.sprinterbe.global.exception.NoDataFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyScrumServiceImpl implements DailyScrumService {

    private final DailyScrumRepository dailyScrumRepository;
    private final UserRepository userRepository;
    private final BacklogRepository backlogRepository;
    private final UserDailyScrumRepository userDailyScrumRepository;
    private final DailyScrumBacklogRepository dailyScrumBacklogRepository;
    private final SprintRepository sprintRepository;
    private final UserProjectRepository userProjectRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DailyScrumInfoResponse> findDailyScrumInfoBySprintId(Long sprintId) {
        //List Empty 가능
        List<DailyScrum> dailyScrums = dailyScrumRepository.findDailyScrumBySprintId(sprintId);
        return dailyScrums.stream().map(DailyScrumInfoResponse::of).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyScrumUserResponse> findDailyScrumUserBySprintId(Long sprintId) {
        List<User> users = dailyScrumRepository.findUsersByDailyScrumId(sprintId);
        return users.stream().map(DailyScrumUserResponse::of).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacklogResponse> findBacklogByDailyScrumId(Long dailyScrumId) {
        // 1. 중간 테이블(DailyScrumBacklog)에서 해당 DailyScrum과 연결된 Backlog 조회
        List<DailyScrumBacklog> dailyScrumBacklogs = dailyScrumBacklogRepository.findByDailyScrumId(dailyScrumId);

        // 2. Backlog 목록 변환 후 응답 DTO 생성
        return dailyScrumBacklogs.stream()
                .map(dsb -> BacklogResponse.of(dsb.getBacklog()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DailyScrumDetailResponse findContentByDailyScrumId(Long dailyScrumId) {
        DailyScrum dailyScrum = dailyScrumRepository.findById(dailyScrumId).orElseThrow(() -> new RuntimeException("Daily scrum not found"));
        return DailyScrumDetailResponse.of(dailyScrum);
    }

    //02.23) 해당 요일에 걸려있는 DailyScrum이 2개일 수 있어서 일단 List로 해놓음
    @Override
    @Transactional(readOnly = true)
    public List<DailyScrumDetailResponse> findDailyScrumByDate(LocalDate startOfDay) {
        List<DailyScrum> dailyScrums = dailyScrumRepository.findByCreatedAt(startOfDay);
        return dailyScrums.stream().map(DailyScrumDetailResponse::of).collect(Collectors.toList());
    }

    @Override
    public DailyScrumPostResponse createDailyScrum(DailyScrumPostRequest request, Long projectId , Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(() -> new NoDataFoundException("sprintId not found"));
        // 1. DailyScrum 엔티티 생성
        DailyScrum dailyScrum = DailyScrum.builder()
                .title(request.getTitle())
                .sprint(sprint)
                .content(request.getContent())
                .build();
        dailyScrumRepository.save(dailyScrum);

        List<User> users = new ArrayList<>();

        // 2. users 목록을 이용해 User 엔티티들을 DailyScrum과 연결
        if (request.getUsers() != null) {
            request.getUsers().forEach(userDto -> {
                User user = userRepository.findById(userDto.getUserId())
                        .orElseThrow(() -> new NoDataFoundException("해당 User가 없습니다. id: " + userDto.getUserId()));
                userDailyScrumRepository.save(UserDailyScrum.builder().user(user).dailyScrum(dailyScrum).build());
                users.add(user);
            });
        }

        // 3. backlogs 목록을 이용해 Backlog 엔티티들을 DailyScrum과 연결
        if (request.getBacklogs() != null) {
            request.getBacklogs().forEach(backlogDto -> {
                Backlog backlog = backlogRepository.findById(backlogDto.getBacklogId())
                        .orElseThrow(() -> new NoDataFoundException("해당 Backlog가 없습니다. id: " + backlogDto.getBacklogId()));

                // 중간 테이블 엔티티 생성 후 저장
                DailyScrumBacklog dsb = DailyScrumBacklog.of(dailyScrum, backlog);
                dailyScrumBacklogRepository.save(dsb);
            });
        }

        // 4. 저장된 DailyScrum 엔티티를 기반으로 응답 DTO 구성
        List<DailyScrumPostResponse.UserDto> userDtos = users.stream()
                .map(user -> DailyScrumPostResponse.UserDto.of(user, projectId))
                .collect(Collectors.toList());

        List<DailyScrumPostResponse.BacklogDto> backlogDtos = dailyScrumBacklogRepository.findByDailyScrum(dailyScrum)
                .stream()
                .map(dsb -> DailyScrumPostResponse.BacklogDto.of(dsb.getBacklog()))
                .collect(Collectors.toList());

        return DailyScrumPostResponse.of(dailyScrum, userDtos, backlogDtos);
    }

    @Override
    public void removeBacklog(Long dailyScrumId, Long backlogId) {
        // DailyScrumBacklog에서 해당 관계 찾기
        DailyScrumBacklog dailyScrumBacklog = dailyScrumBacklogRepository.findByDailyScrumAndBacklog(dailyScrumId, backlogId)
                .orElseThrow(() -> new NoDataFoundException("해당 DailyScrum과 Backlog의 연결이 없습니다."));

        // 관계 삭제
        dailyScrumBacklogRepository.delete(dailyScrumBacklog);
    }


    @Override
    public BacklogResponse addBacklogToDailyScrum(Long dailyScrumId, Long backlogId) {
        // DailyScrum 찾기
        DailyScrum dailyScrum = dailyScrumRepository.findById(dailyScrumId)
                .orElseThrow(() -> new NoDataFoundException("해당 DailyScrum이 없습니다. id: " + dailyScrumId));

        // Backlog 찾기
        Backlog backlog = backlogRepository.findById(backlogId)
                .orElseThrow(() -> new NoDataFoundException("해당 Backlog가 없습니다. id: " + backlogId));

        // DailyScrumBacklog 관계 저장
        DailyScrumBacklog dailyScrumBacklog = DailyScrumBacklog.builder().dailyScrum(dailyScrum).backlog(backlog).build();
        dailyScrumBacklogRepository.save(dailyScrumBacklog);

        // 응답 DTO 변환 후 반환
        return BacklogResponse.of(backlog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyScrumUserResponse> findUsersNotInDailyScrum(Long projectId, Long dailyScrumId) {
        // 1. 프로젝트에 속한 모든 유저 조회
        List<User> projectUsers = userProjectRepository.findByProjectProjectId(projectId)
                .stream()
                .map(UserProject::getUser)
                .collect(Collectors.toList());

        // 2. DailyScrum에 속한 유저들 조회
        List<User> dailyScrumUsers = userDailyScrumRepository.findByDailyScrumId(dailyScrumId)
                .stream()
                .map(UserDailyScrum::getUser)
                .collect(Collectors.toList());

        // 3. 프로젝트 유저 중 DailyScrum에 속하지 않은 유저들만 필터링
        List<User> availableUsers = projectUsers.stream()
                .filter(user -> !dailyScrumUsers.contains(user))
                .collect(Collectors.toList());

        // 4. 응답 DTO로 변환
        return availableUsers.stream()
                .map(DailyScrumUserResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public DailyScrumUserResponse addUserToDailyScrum(Long dailyScrumId, Long userId) {
        // DailyScrum 찾기
        DailyScrum dailyScrum = dailyScrumRepository.findById(dailyScrumId)
                .orElseThrow(() -> new NoDataFoundException("해당 DailyScrum이 없습니다. id: " + dailyScrumId));

        // User 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("해당 User가 없습니다. id: " + userId));

        // UserDailyScrum 관계 저장
        UserDailyScrum userDailyScrum = UserDailyScrum.builder()
                .user(user)
                .dailyScrum(dailyScrum)
                .build();
        userDailyScrumRepository.save(userDailyScrum);

        // 응답 DTO 변환 후 반환
        return DailyScrumUserResponse.of(user);
    }

    @Override
    public void removeUserFromDailyScrum(Long dailyScrumId, Long userId) {
        // UserDailyScrum에서 해당 관계 찾기
        UserDailyScrum userDailyScrum = userDailyScrumRepository.findByDailyScrumIdAndUserId(dailyScrumId, userId)
                .orElseThrow(() -> new NoDataFoundException("해당 DailyScrum과 User의 연결이 없습니다."));

        // 관계 삭제
        userDailyScrumRepository.delete(userDailyScrum);
    }

    @Override
    public DailyScrumDetailResponse updateContent(Long dailyScrumId, String content) {
        // DailyScrum 찾기
        DailyScrum dailyScrum = dailyScrumRepository.findById(dailyScrumId)
                .orElseThrow(() -> new NoDataFoundException("해당 DailyScrum이 없습니다. id: " + dailyScrumId));

        // 내용 업데이트
        dailyScrum.setContent(content);
        dailyScrumRepository.save(dailyScrum);

        // 응답 DTO 변환 후 반환
        return DailyScrumDetailResponse.of(dailyScrum);
    }




}
