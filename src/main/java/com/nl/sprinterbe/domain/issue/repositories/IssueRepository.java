package com.nl.sprinterbe.domain.issue.repositories;

import com.nl.sprinterbe.domain.issue.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

}
