package com.nl.sprinterbe.domain.backlog.dao;

import com.nl.sprinterbe.domain.backlog.dto.BacklogSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nl.sprinterbe.domain.backlog.entity.QBacklog.backlog;
import static com.nl.sprinterbe.domain.project.entity.QProject.project;
import static com.nl.sprinterbe.domain.sprint.entity.QSprint.sprint;

@Repository
@RequiredArgsConstructor
public class BacklogSearchQueryRepositoryImpl implements BacklogSearchQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<BacklogSearchResponse> searchBacklog(String keyword) {
        /*
        * 백로그id, 프로젝트id, 스프린트id, 제목을 검색하여 결과를 반환하는 메소드
        * */
        List<BacklogSearchResponse> backlogSearchResponses = query
                .select(
                        Projections.constructor(
                                BacklogSearchResponse.class,
                                backlog.backlogId.as("backlogId"),
                                backlog.title.as("title"),
                                project.projectId.as("projectId"),
                                sprint.sprintId.as("sprintId")
                        )
                )
                .from(backlog)
                .innerJoin(backlog.sprint, sprint) // 백로그와 스프린트 테이블을 조인
                .innerJoin(sprint.project, project) // 스프린트와 프로젝트 테이블을 조인
                .where(
                        containsKeyword(keyword)  // 키워드 검색 조건 추가
                )
                .fetch();

        return backlogSearchResponses;
    }
    private BooleanExpression containsKeyword(String keyword) {
        return keyword == null || keyword.isEmpty()
                ? null
                : backlog.title.contains(keyword);  // title에 키워드 포함 여부 확인
    }
}
