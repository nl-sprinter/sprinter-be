package com.nl.sprinterbe.service;

import com.nl.sprinterbe.dto.ProjectDTO;
import com.nl.sprinterbe.entity.Project;
import com.nl.sprinterbe.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    //프로젝트 생성(gpt없이)
    public void createProject(ProjectDTO projectDTO) {
        Project project = Project.builder()
                .projectName(projectDTO.getProjectName())
                .createdAt(projectDTO.getCreatedAt())
                .build();
        projectRepository.save(project);
    }

    //프로젝트 생성(gpt있이)

    //프로젝트 수정
    public void updateProject(Long projectId, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        project.setProjectName(projectDTO.getProjectName());
        project.setCreatedAt(projectDTO.getCreatedAt());

        projectRepository.save(project);
    }

    //프로젝트 삭제
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    //프로젝트 리스트 조회
    public List<ProjectDTO> getProjects() {
        return projectRepository.findAll().stream()
                .map(project -> ProjectDTO.builder()
                        .projectId(project.getProjectId())
                        .projectName(project.getProjectName())
                        .createdAt(project.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

}
