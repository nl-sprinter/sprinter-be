package com.nl.sprinterbe.domain.project.application;

import com.nl.sprinterbe.domain.project.dto.ProjectDto;
import com.nl.sprinterbe.domain.user.dto.UserDto;
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

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    public final ProjectRepository projectRepository;
    public final UserRepository userRepository;
    public final UserProjectRepository userProjectRepository;

    public Project createProject(StartingDataDto startingDataDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String projectName = startingDataDto.getProject().getProjectName();
        int sprintCount = startingDataDto.getSprint().getSprintCount(); // 스프린트 개수
        int sprintDuration = startingDataDto.getSprint().getSprintDuration(); // 1개 스프린트 기간

        // 도메인 객체가 자체적으로 프로젝트와 스프린트를 생성
        Project project = Project.createProject(
                projectName,
                sprintCount,
                sprintDuration
        );

        project = projectRepository.save(project);

//        List<Sprint> sprints = project.getSprints();
//        for (Sprint sprint : sprints) {
//            sprint.백로그생성();
//        }


        // TODO : 백로그 아이템 빼서 프로젝트에 넣어야함
//        List<StartingDataDto.BacklogItem> backlogList = startingDataDto.getBacklog();
//        for (StartingDataDto.BacklogItem backlogItem : backlogList) {
//
//        }
        project = projectRepository.save(project);

        UserProject userProject = new UserProject(user, project, true);
        userProjectRepository.save(userProject);

        return project;
    }

    //프로젝트 유저추가
    public void addUserToProject(UserDto userDTO, Long projectId) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDTO.getEmail()));

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

    public List<UserDto> getUsers(Long projectId) {
        List<User> users = userProjectRepository.findByProjectProjectId(projectId)
                .stream()
                .map(UserProject::getUser)
                .toList();

        return users.stream()
                .map(user -> new UserDto(user.getEmail(), user.getNickname()))
                .toList();
    }

    public void updateProject(Long projectId, ProjectDto projectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        project.setProjectName(projectDTO.getProjectName());
        projectRepository.save(project);
    }

}
