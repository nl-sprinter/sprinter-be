package com.nl.sprinterbe.domain.userProject.dao;

import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.userProject.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject,Long> {
    List<UserProject> findByUserUserId(Long userId);
    List<UserProject> findByProjectProjectId(Long projectId);
    List<UserProject> findByProject(Project project);
}
