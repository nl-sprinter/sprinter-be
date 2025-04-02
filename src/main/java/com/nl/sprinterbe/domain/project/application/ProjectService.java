package com.nl.sprinterbe.domain.project.application;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.backlogcomment.dao.BacklogCommentRepository;
import com.nl.sprinterbe.domain.dailyscrum.dao.DailyScrumBacklogRepository;
import com.nl.sprinterbe.domain.dailyscrum.dao.UserDailyScrumRepository;
import com.nl.sprinterbe.domain.project.dto.ProjectProgressResponse;
import com.nl.sprinterbe.domain.project.dto.SoftwareEngineeringElementResponse;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.task.dao.TaskRepository;
import com.nl.sprinterbe.domain.user.dto.UserInfoResponse;
import com.nl.sprinterbe.domain.user.dto.UserInfoWithTeamLeaderResponse;
import com.nl.sprinterbe.domain.userbacklog.dao.UserBacklogRepository;
import com.nl.sprinterbe.dto.*;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.userproject.entity.UserProject;
import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.userproject.dao.UserProjectRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.global.exception.project.DuplicateProjectNameException;
import com.nl.sprinterbe.global.exception.project.ProjectNotFoundException;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
import com.nl.sprinterbe.global.exception.userproject.UserIsNotProjectLeaderException;
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
    private final DailyScrumBacklogRepository dailyScrumBacklogRepository;
    private final UserDailyScrumRepository userDailyScrumRepository;
    private final UserBacklogRepository userBacklogRepository;
    private final BacklogCommentRepository backlogCommentRepository;
    private final TaskRepository taskRepository;

    public void createProject(StartingDataDto startingDataDto, Long userId) {
        // 유저 검증
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 프로젝트 생성
        Project project = Project.builder()
                .createdAt(LocalDateTime.now())
                .projectName(startingDataDto.getProject().getProjectName())
                .sprintPeriod(startingDataDto.getSprint().getSprintPeriod())
                .build();

        // 스프린트 생성 및 현재 프로젝트에 연결
        List<Sprint> sprints = createSprintsForProject(project, startingDataDto.getSprint());
        sprints.forEach(project::addSprint);

        // 프로젝트 저장 (transient상태인 project 엔티티를 참조하려고 해서 발생하는 문제를 해결하기 위함)
        project = projectRepository.save(project);

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


    // 프로젝트 유저추가
    public void addUserToProject(Long userId, Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        UserProject userProject = new UserProject(user, project, false);
        userProjectRepository.save(userProject);
    }

    //프로젝트 삭제
    public void deleteProject(Long projectId, Long userId) {
        if (!checkUserIsProjectLeader(projectId, userId)) {
            throw new UserIsNotProjectLeaderException();
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<UserProject> userProjects = userProjectRepository.findByProject(project);

        userProjectRepository.deleteAll(userProjects); // UserProject 삭제
        projectRepository.delete(project); // 프로젝트 삭제
    }

    // 프로젝트에 속한 유저 정보 가져오기
    public List<UserInfoWithTeamLeaderResponse> getUsersInProject(Long projectId) {
        return userProjectRepository.findProjectUsersByProjectId(projectId);
    }

    // 프로젝트 이름 변경
    public void updateProjectName(Long projectId, String newProjectName) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        // 중복 검사
        boolean exists = projectRepository.existsByProjectName(newProjectName);
        if (exists) {
            throw new DuplicateProjectNameException("Project name already exists: " + newProjectName);
        }

        project.setProjectName(newProjectName);
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
            LocalDate endDate = startDate.plusDays(sprintInfo.getSprintPeriod());

            Sprint sprint = Sprint.builder()
                    .sprintName("Sprint " + i)
                    .startDate(startDate)
                    .endDate(endDate)
                    .sprintOrder((long) i)
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

    public int getSprintPeriod(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        return project.getSprintPeriod();
    }

    public void updateSprintPeriod(Long projectId, int sprintPeriod) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        project.setSprintPeriod(sprintPeriod);
    }

    public List<UserInfoResponse> searchUserToAddProject(String keyword, Long projectId) {
        return projectRepository.searchUsersNotInProject(keyword, projectId);
    }

    public void deleteUserInProject(Long projectId, Long teamLeaderUserId, Long targetUserId) {
        if (!checkUserIsProjectLeader(projectId, teamLeaderUserId)) {
            throw new UserIsNotProjectLeaderException();
        }
        userProjectRepository.deleteByProjectIdAndUserId(projectId, targetUserId);
        userBacklogRepository.deleteByUserUserId(targetUserId);
        userDailyScrumRepository.deleteByUserUserId(targetUserId);
        backlogCommentRepository.deleteByUserUserId(targetUserId);
        taskRepository.updateUserIdToNullWhenUserGoOutProject(targetUserId);
    }

    public void deleteUserInProject(Long projectId, Long targetUserId) {
        userProjectRepository.deleteByProjectIdAndUserId(projectId, targetUserId);
        userBacklogRepository.deleteByUserUserId(targetUserId);
        userDailyScrumRepository.deleteByUserUserId(targetUserId);
        backlogCommentRepository.deleteByUserUserId(targetUserId);
        taskRepository.updateUserIdToNullWhenUserGoOutProject(targetUserId);
    }

    public boolean checkUserIsProjectLeader(Long projectId, Long userId) {
        return userProjectRepository.findByProjectProjectIdAndUserUserId(projectId, userId)
                .orElseThrow(UserNotFoundException::new)
                .getIsProjectLeader();
    }

    public ProjectProgressResponse getProjectProgressPercent(Long projectId) {
        int x = backlogRepository.countIsFinishedBacklogs(projectId);
        int y = backlogRepository.countByProjectId(projectId);
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        if(y==0){
            projectProgressResponse.setPercent(0);
        }else{
            projectProgressResponse.setPercent((int) ((double) x / y * 100));
        }
        return projectProgressResponse;
    }

    public List<SoftwareEngineeringElementResponse> getBurnDownChartAndVelocityChartData(Long projectId) {
        /*예상 : 백로그의 모든 가중치를 합쳐 스프린트 1으로 보내고 스프린트 갯수로 나눈 값을 빼서 리스트에 각각 담는다.
         실제 : 스프린트당 끝낸 백로그의 가중치를 전체에서 뺌
         밸로시티: 스프린트당 끝낸 백로그의 가중치*/
        List<Sprint> sprints = sprintRepository.findAllByProjectProjectId(projectId);

        List<SoftwareEngineeringElementResponse> softwareEngineeringElementResponses = new ArrayList<>();
        Long totalWeight = 0L;
        Long totalfinishedWeight = 0L;
        Map<Long, Long> sprintIdToVelocity = new HashMap<>();

        for(Sprint sprint : sprints){
            List<Backlog> backlogs = backlogRepository.findBySprintSprintId(sprint.getSprintId());
            Long finishedWeight = 0L;
            for(Backlog backlog : backlogs){
                totalWeight += backlog.getWeight();
                if(backlog.getIsFinished()){
                    finishedWeight += backlog.getWeight();
                }
            }
            sprintIdToVelocity.put(sprint.getSprintId(), finishedWeight);
            totalfinishedWeight += finishedWeight;
        }

        double estimateSize = totalWeight / sprints.size();
        double totalWeightCopy = totalWeight;

        for(Sprint sprint : sprints){

            Long velocity = sprintIdToVelocity.get(sprint.getSprintId());
            SoftwareEngineeringElementResponse softwareEngineeringElementResponse = new SoftwareEngineeringElementResponse();
            softwareEngineeringElementResponse.setRealBurndownPoint(Math.round(totalWeightCopy - velocity));
            softwareEngineeringElementResponse.setEstimatedBurndownPoint(Math.round(totalWeightCopy - estimateSize));
            softwareEngineeringElementResponse.setSprintOrder(sprint.getSprintOrder());
            softwareEngineeringElementResponse.setVelocity(velocity);
            softwareEngineeringElementResponse.setSprintId(sprint.getSprintId());

            if(sprint.getSprintOrder() == sprints.size()){
                softwareEngineeringElementResponse.setEstimatedBurndownPoint(0L);
                if(totalfinishedWeight == totalWeight){
                    softwareEngineeringElementResponse.setRealBurndownPoint(0L);
                }
            }

            softwareEngineeringElementResponses.add(softwareEngineeringElementResponse);
            totalWeightCopy -= estimateSize;
        }

        return softwareEngineeringElementResponses;
    }
}
