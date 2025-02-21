package com.nl.sprinterbe.domain.userBacklog.dao;

import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.userBacklog.entity.UserBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBacklogRepository extends JpaRepository<UserBacklog, Long> {
    @Query("SELECT u.user FROM UserBacklog u WHERE u.backlog.backlogId = :backlogId")
    List<User> findUsersByBacklogId(@Param("backlogId") Long backlogId);
}
