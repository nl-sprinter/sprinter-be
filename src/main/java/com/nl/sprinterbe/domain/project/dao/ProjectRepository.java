package com.nl.sprinterbe.domain.project.dao;

import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.user.dto.UserInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // 프로젝트 이름 중복 여부
    boolean existsByProjectName(String name);

    // 현재 프로젝트에 소속되어 있지 않은 유저를 keyword(email or nickname)로 조회
    @Query("SELECT new com.nl.sprinterbe.domain.user.dto.UserInfoResponse(u.userId, u.email, u.nickname, u.role)" +
            " FROM User u" +
            " WHERE (LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "    OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            " AND" +
            " u.userId NOT IN (SELECT up.user.userId FROM UserProject up WHERE up.project.projectId = :projectId)")
    List<UserInfoResponse> searchUsersNotInProject(@Param("keyword") String keyword, @Param("projectId") Long projectId);
}
