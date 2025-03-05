package com.nl.sprinterbe.domain.project.application;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.project.dto.ProjectNameDto;
import com.nl.sprinterbe.domain.project.dto.ProjectUserRequest;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.user.dto.UserDetailResponse;
import com.nl.sprinterbe.dto.*;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.userProject.entity.UserProject;
import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.userProject.dao.UserProjectRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.global.exception.project.ProjectNotFoundException;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final BacklogRepository backlogRepository;

    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;

    public void createProject(StartingDataDto startingDataDto, Long userId) {
        // 유저 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        // 프로젝트 생성
        Project project = Project.builder()
                .createdAt(LocalDateTime.now())
                .projectName(startingDataDto.getProject().getProjectName())
                .build();

        // 스프린트 생성 및 현재 프로젝트에 연결
        List<Sprint> sprints = createSprintsForProject(project, startingDataDto.getSprint());
        // 디버그
        log.debug("project = {}", project);
        for (Sprint sprint : sprints) {
            log.debug("sprint = {}", sprint);
        }
        sprints.forEach(project::addSprint); // 예외터짐
        // 스프린트 저장
        sprintRepository.saveAll(sprints);

        // 프로젝트 저장
        project = projectRepository.save(project);

        // 백로그 생성 및 연결
        List<Backlog> backlogs = createBacklogsForSprints(startingDataDto.getBacklog(), sprints);
        // 백로그 저장
        backlogRepository.saveAll(backlogs);

        // UserProject 관계 생성
        UserProject userProject = new UserProject(user, project, true);
        userProjectRepository.save(userProject);
    }


    //프로젝트 유저추가
    public void addUserToProject(UserDetailResponse request, Long projectId) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException());

        UserProject userProject = new UserProject(user, project, false);
        userProjectRepository.save(userProject);
    }

    //프로젝트 삭제
    public void deleteProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException());

        List<UserProject> userProjects = userProjectRepository.findByProject(project);
        Optional<Long> leaderUserId = userProjects.stream()
                .filter(UserProject::getIsProjectLeader)
                .map(userProject -> userProject.getUser().getUserId())
                .findFirst();

        Long leaderId = leaderUserId.orElseThrow(() -> new RuntimeException("프로젝트 리더를 찾을 수 없습니다."));

//        if (!leaderId.equals(userId)) {
//            throw new RuntimeException("Project leader not found");
//        }

        userProjectRepository.deleteAll(userProjects); // UserProject 삭제
        projectRepository.delete(project); // 프로젝트 삭제
    }

    public List<ProjectUserRequest> getUsers(Long projectId) {
        List<User> users = userProjectRepository.findByProjectProjectId(projectId)
                .stream()
                .map(UserProject::getUser)
                .toList();

        return users.stream()
                .map(user -> new ProjectUserRequest(user.getUserId(),user.getRole(),user.getEmail(), user.getNickname()))
                .toList();
    }

    public void updateProject(Long projectId, ProjectNameDto projectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException());

        project.setProjectName(projectDTO.getProjectName());
        projectRepository.save(project);
    }


    /**
     * 프로젝트 생성 시 스프린트 생성
     *
     * @param project    생성중인 프로젝트
     * @param sprintInfo
     * @return 스프린트 리스트
     */
    private List<Sprint> createSprintsForProject(Project project, StartingDataDto.SprintInfo sprintInfo) {
        List<Sprint> sprints = new ArrayList<>();
        LocalDate startDate = LocalDate.now();

        for (int i = 1; i <= sprintInfo.getSprintCount(); i++) {
            LocalDate endDate = startDate.plusDays(sprintInfo.getSprintDuration());

            Sprint sprint = Sprint.builder()
                    .sprintName("Sprint " + i)
                    .startDate(startDate)
                    .endDate(endDate)
                    .project(project)
                    .build();

            sprints.add(sprint);
            startDate = endDate.plusDays(1); // 다음 스프린트는 하루 뒤부터 시작
        }

        return sprints;
    }

    /**
     * 프로젝트 생성 시 스프린트 생성 시 백로그 생성
     *
     * @param backlogItems StartingDataDto 에서 받아온 백로그들
     * @param sprints      스프린트 리스트
     */
    private List<Backlog> createBacklogsForSprints(List<StartingDataDto.BacklogItem> backlogItems, List<Sprint> sprints) {
        if (backlogItems == null || backlogItems.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Integer, Sprint> sprintMap = sprints.stream()
                .collect(Collectors.toMap(
                        sprint -> Integer.parseInt(sprint.getSprintName().split(" ")[1]),
                        sprint -> sprint
                ));

        List<Backlog> backlogs = new ArrayList<>();
        
        backlogItems.forEach(item -> {
            Sprint sprint = sprintMap.get(item.getSprintNumber());
            Backlog backlog = Backlog.builder()
                    .title(item.getTitle())
                    .weight(item.getWeight())
                    .isFinished(false)
                    .build();
            
            sprint.addBacklog(backlog); // 스프린트에 백로그 할당
            backlogs.add(backlog);
        });

        return backlogs;
    }

}
