package com.nl.sprinterbe.domain.sprint.dao;

import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findByProjectProjectId(Long projectId);

}
