package com.nl.sprinterbe.domain.project.application;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.project.dto.ProjectDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;
    private final BacklogRepository backlogRepository;

    // TODO: 프로젝트 생성
    public void createProject(StartingDataDto startingDataDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Project project = new Project();
        project.setProjectName(startingDataDto.getProject().getProjectName());
        // TODO : startingDataDto에서 스프린트 개수랑 스프린트별 만들어진 프로덕트백로그 뽑아서 설정해줘야함!!!
        Integer sprintCount = startingDataDto.getSprint().getSprintCount(); // 스프린트 개수

        // 해당 스프린트 갯수만큼 미리 객체는 만들어놓고 각각에 project는 세팅
        Map<Integer, Sprint> sprintMap = new HashMap<>();
        for(int i=1; i<=sprintCount; i++) {
            Sprint sprint = new Sprint();
            sprint.setProject(project);
            sprintMap.put(i, sprint);
        }

        //<Sprint 번호 , 해당 Backlog List>
        Map<Integer, List<StartingDataDto.BacklogItem>> productBacklogListDtoMap = startingDataDto.getProductBacklogListMap();
        for (Map.Entry<Integer, List<StartingDataDto.BacklogItem>> entry : productBacklogListDtoMap.entrySet()) {
            Integer sprintNumber = entry.getKey();
            List<StartingDataDto.BacklogItem> productBacklogList = entry.getValue();
            //여기에서 for문 돌면서 하나씩 sprintNumber에 맞는 스프린트 가져와서 프로젝트-스프린트 연관관계가 주입된 스프린트 객체를 Backlog 객체 연관관계 필드에 주입
            productBacklogList.forEach(backlog ->{
                Backlog b = Backlog.builder().title(backlog.getTitle()).weight(backlog.getWeight()).build();
                b.setSprint(sprintMap.get(backlog.getSprintNumber()));
                backlogRepository.save(b);
            } );
            // TODO: SprintNumber에 맞는 스프린트에 productBacklogList 할당하기
        }


        //유저와 프로젝트 영속화 / 저장
        UserProject userProject = new UserProject(user, project, true);
        userProjectRepository.save(userProject);
    }

    //프로젝트 유저추가
    public void addUserToProject(UserDetailResponse userDetailResponse, Long projectId) {
        User user = userRepository.findByEmail(userDetailResponse.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDetailResponse.getEmail()));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        UserProject userProject = new UserProject(user, project, false);
        userProjectRepository.save(userProject);
    }

    //프로젝트 삭제
    public void deleteProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        List<UserProject> userProjects = userProjectRepository.findByProject(project);
        Optional<Long> leaderUserId = userProjects.stream()
                .filter(UserProject::getIsProjectLeader)
                .map(userProject -> userProject.getUser().getUserId())
                .findFirst();

        Long leaderId = leaderUserId.orElseThrow(() -> new RuntimeException("프로젝트 리더를 찾을 수 없습니다."));

        if (!leaderId.equals(userId)) {
            throw new RuntimeException("Project leader not found");
        }

        userProjectRepository.deleteAll(userProjects); // UserProject 삭제
        projectRepository.delete(project); // 프로젝트 삭제
    }

    public List<UserDetailResponse> getUsers(Long projectId) {
        List<User> users = userProjectRepository.findByProjectProjectId(projectId)
                .stream()
                .map(UserProject::getUser)
                .toList();

        return users.stream()
                .map(user -> new UserDetailResponse(user.getEmail(), user.getNickname()))
                .toList();
    }

    public void updateProject(Long projectId, ProjectDto projectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        project.setProjectName(projectDTO.getProjectName());
        projectRepository.save(project);
    }

}
