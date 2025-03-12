package com.nl.sprinterbe.domain.userproject.dao;

import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.user.dto.UserInfoWithTeamLeaderResponse;
import com.nl.sprinterbe.domain.userproject.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject,Long> {

    @Query("SELECT up FROM UserProject up" +
            " JOIN FETCH up.project" +
            " WHERE up.user.userId=:userId")
    List<UserProject> findByUserUserId(Long userId);

    List<UserProject> findByProjectProjectId(Long projectId);

    List<UserProject> findByProject(Project project);

    @Query("SELECT new com.nl.sprinterbe.domain.user.dto.UserInfoWithTeamLeaderResponse(u.userId, u.email, u.nickname, u.role, up.isProjectLeader)" +
            " FROM UserProject up JOIN up.user u" +
            " WHERE up.project.projectId = :projectId")
    List<UserInfoWithTeamLeaderResponse> findProjectUsersByProjectId(@Param("projectId") Long projectId);

    Optional<UserProject> findByProjectProjectIdAndUserUserId(Long projectId, Long userId);

    @Modifying
    @Query("DELETE FROM UserProject up" +
            " WHERE up.user.userId = :userId" +
            " AND up.project.projectId = :projectId")
    void deleteByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);
}
