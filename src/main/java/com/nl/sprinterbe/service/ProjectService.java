package com.nl.sprinterbe.service;

import com.nl.sprinterbe.dto.ProjectDto;
import com.nl.sprinterbe.dto.SprintDto;
import com.nl.sprinterbe.entity.Project;
import com.nl.sprinterbe.entity.UserProject;
import com.nl.sprinterbe.repository.ProjectRepository;
import com.nl.sprinterbe.repository.UserProjectRepository;
import com.nl.sprinterbe.dto.UserDto;
import com.nl.sprinterbe.entity.User;
import com.nl.sprinterbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    public final ProjectRepository projectRepository;
    public final UserRepository userRepository;
    public final UserProjectRepository userProjectRepository;

    // 프로젝트 생성
    public void createProject(ProjectDto projectDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Project project = new Project();
        project.setProjectName(projectDTO.getProjectName());

        project = projectRepository.save(project);

        UserProject userProject = new UserProject(user, project, true);
        userProjectRepository.save(userProject);
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
