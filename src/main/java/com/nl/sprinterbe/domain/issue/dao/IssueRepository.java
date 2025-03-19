package com.nl.sprinterbe.domain.issue.dao;

import com.nl.sprinterbe.domain.issue.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> , IssueSearchQueryRepository {

    List<Issue> findByBacklogBacklogId(Long backlogId);
}
