package com.nl.sprinterbe.service;

import com.nl.sprinterbe.dto.ProjectDTO;
import com.nl.sprinterbe.entity.Project;
import com.nl.sprinterbe.entity.UserProject;
import com.nl.sprinterbe.repository.ProjectRepository;
import com.nl.sprinterbe.repository.UserProjectRepository;
import com.nl.sprinterbe.dto.UserDTO;
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

    //프로젝트 생성
    public void createProject(ProjectDTO projectDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Project project = new Project();
        project.setProjectName(projectDTO.getProjectName());

        // 연관 관계 추가
        UserProject userProject = new UserProject(user, project, true);
        project.addUserProject(userProject); // 편의 메서드 활용

        projectRepository.save(project); // CascadeType.ALL 덕분에 UserProject도 자동 저장됨
    }

    //프로젝트 유저추가
    public void addUserToProject(UserDTO userDTO, Long projectId) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDTO.getEmail()));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        UserProject userProject = new UserProject(user, project, false);
        project.getUserProjects().add(userProject);

        projectRepository.save(project);
    }

    //프로젝트 삭제
    public void deleteProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        List<UserProject> userProjects = project.getUserProjects();
        Optional<Long> leaderUserId = userProjects.stream()
                .filter(UserProject::getIsProjectLeader)
                .map(userProject -> userProject.getUser().getUserId())
                .findFirst();

        Long leaderId = leaderUserId.orElseThrow(() -> new RuntimeException("프로젝트 리더를 찾을 수 없습니다."));

        if (!leaderId.equals(userId)) {
            throw new RuntimeException("Project leader not found");
        }

        userProjects.clear();

        projectRepository.delete(project);
    }

    public List<UserDTO> getUsers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        return project.getUserProjects().stream()
                .map(userProject -> {
                    User user = userProject.getUser();
                    return new UserDTO(user.getEmail(), user.getNickname());
                })
                .toList();
    }

    public void updateProject(Long projectId, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        project.setProjectName(projectDTO.getProjectName());
        projectRepository.save(project);
    }
}
