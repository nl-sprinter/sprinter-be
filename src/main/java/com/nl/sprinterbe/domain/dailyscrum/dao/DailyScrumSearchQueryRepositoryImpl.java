package com.nl.sprinterbe.domain.dailyscrum.dao;

import com.nl.sprinterbe.domain.dailyscrum.dto.DailyScrumSearchResponse;
import com.nl.sprinterbe.domain.dailyscrum.dto.QDailyScrumSearchResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nl.sprinterbe.domain.dailyscrum.entity.QDailyScrum.dailyScrum;
import static com.nl.sprinterbe.domain.project.entity.QProject.project;
import static com.nl.sprinterbe.domain.sprint.entity.QSprint.sprint;

@Repository
@RequiredArgsConstructor
public class DailyScrumSearchQueryRepositoryImpl implements DailyScrumSearchQueryRepository {
    private final JPAQueryFactory query;
    @Override
    public List<DailyScrumSearchResponse> searchDailyScrum(String keyword, Long projectId) {
        List<DailyScrumSearchResponse> dailyScrumSearchResponses = query
                .select(
                        new QDailyScrumSearchResponse(
                                project.projectId.as("projectId"),
                                dailyScrum.dailyScrumId.as("dailyScrumId"),
                                sprint.sprintId.as("sprintId"),
                                dailyScrum.content.as("content")
                        )
                )
                .from(dailyScrum)
                .innerJoin(dailyScrum.sprint, sprint)
                .innerJoin(sprint.project, project)
                .where(
                        containsKeyword(keyword),
                        eqProjectId(projectId)
                )
                .fetch();

        return dailyScrumSearchResponses;
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword == null || keyword.isEmpty()
                ? null
                : dailyScrum.content.contains(keyword);
    }

    private BooleanExpression eqProjectId(Long projectId) {
        return projectId == null
                ? null
                : project.projectId.eq(projectId);
    }
}
