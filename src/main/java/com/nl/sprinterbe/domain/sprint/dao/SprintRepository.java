package com.nl.sprinterbe.domain.sprint.dao;

import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findAllByProjectProjectId(Long projectId);

    Sprint findBySprintId(Long sprintId);

    // 프로젝트 내에서 가장 최신의 스프린트를 가져오는 쿼리
    @Query(value = "SELECT * FROM sprint s" +
            " WHERE s.project_id = :projectId" +
            " ORDER BY s.end_date DESC LIMIT 1"
            , nativeQuery = true)
    Sprint findLatestSprint(@Param("projectId") Long projectId);
}
