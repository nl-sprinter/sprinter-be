package com.nl.sprinterbe.service;

import com.nl.sprinterbe.dto.ProjectDTO;
import com.nl.sprinterbe.entity.Project;
import com.nl.sprinterbe.entity.UserProject;
import com.nl.sprinterbe.repository.ProjectRepository;
import com.nl.sprinterbe.repository.UserProjectRepository;
import com.nl.sprinterbe.user.dto.UserDTO;
import com.nl.sprinterbe.user.entity.User;
import com.nl.sprinterbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    public final ProjectRepository projectRepository;
    public final UserProjectRepository userProjectRepository;
    public final UserRepository userRepository;

    //프로젝트 생성
    @Transactional
    public void createProject(ProjectDTO projectDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Project project = new Project();
        project.setProjectName(projectDTO.getProjectName());
        projectRepository.save(project);

        UserProject userProject = new UserProject(user, project, true);
        userProjectRepository.save(userProject);

    }

    //프로젝트 유저추가
    @Transactional
    public void addUserToProject(UserDTO userDTO, Long projectId) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDTO.getEmail()));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        UserProject userProject = new UserProject(user, project, false);
        userProjectRepository.save(userProject);
    }

    //프로젝트 삭제
    @Transactional
    public void deleteProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        List<UserProject> userProjects = userProjectRepository.findByProject(project);
        Optional<Long> leaderUserId = userProjects.stream()
                .filter(UserProject::getIsProjectLeader)
                .map(userproject->userproject.getUser().getUserId())
                .findFirst();

        Long leaderId = leaderUserId.orElseThrow(() -> new RuntimeException("프로젝트 리더를 찾을 수 없습니다."));

        if(!leaderId.equals(userId)) {
            throw new RuntimeException("Project leader not found");
        }

        userProjectRepository.deleteByProject(project);
        projectRepository.delete(project);
    }

}
