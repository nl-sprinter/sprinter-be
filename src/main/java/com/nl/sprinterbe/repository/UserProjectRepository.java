package com.nl.sprinterbe.repository;

import com.nl.sprinterbe.entity.Project;
import com.nl.sprinterbe.entity.UserProject;
import com.nl.sprinterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject,Long> {
    void deleteByProject(Project project);
    List<UserProject> findByProject(Project project);
}
