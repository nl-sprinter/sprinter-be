package com.nl.sprinterbe.domain.issue.dao;

import com.nl.sprinterbe.domain.issue.entity.Issue;
import com.nl.sprinterbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("SELECT i.user FROM Issue i WHERE u.backlog.backlogId = :backlogId")
    List<User> findUsersByBacklogId(@Param("backlogId") Long backlogId);
}
