package com.nl.sprinterbe.domain.project.dao;

import com.nl.sprinterbe.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // 프로젝트 이름 중복 여부
    boolean existsByProjectName(String name);
}
