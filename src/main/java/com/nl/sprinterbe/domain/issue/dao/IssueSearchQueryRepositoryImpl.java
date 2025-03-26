package com.nl.sprinterbe.domain.issue.dao;

import com.nl.sprinterbe.domain.issue.dto.IssueSearchResponse;
import com.nl.sprinterbe.domain.issue.dto.QIssueSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nl.sprinterbe.domain.backlog.entity.QBacklog.backlog;
import static com.nl.sprinterbe.domain.issue.entity.QIssue.issue;
import static com.nl.sprinterbe.domain.project.entity.QProject.project;
import static com.nl.sprinterbe.domain.sprint.entity.QSprint.sprint;

@Repository
@RequiredArgsConstructor
public class IssueSearchQueryRepositoryImpl implements IssueSearchQueryRepository {

    private final JPAQueryFactory query;
    @Override
    public List<IssueSearchResponse> searchIssue(String keyword, Long projectId) {
        /*
        * 이슈가 속한 projectid, sprintid, backlogid, 내용을 검색하여 결과를 반환하는 메소드
        * */

        List<IssueSearchResponse> issueSearchResponses = query
                .select(
                        new QIssueSearchResponse(
                                issue.content.as("content"),
                                project.projectId.as("projectId"),
                                sprint.sprintId.as("sprintId"),
                                backlog.backlogId.as("backlogId")
                        )
                )
                .from(issue)
                .innerJoin(issue.backlog, backlog) // 이슈와 백로그 테이블을 조인
                .innerJoin(backlog.sprint, sprint) // 백로그와 스프린트 테이블을 조인
                .innerJoin(sprint.project, project) // 스프린트와 프로젝트 테이블을 조인
                .where(
                        containsKeyword(keyword),
                        eqProjectId(projectId)
                )
                .fetch();

        return issueSearchResponses;
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword == null || keyword.isEmpty()
                ? null
                : issue.content.contains(keyword);
    }

    private BooleanExpression eqProjectId(Long projectId) {
        return projectId == null
                ? null
                : project.projectId.eq(projectId);
    }
}
