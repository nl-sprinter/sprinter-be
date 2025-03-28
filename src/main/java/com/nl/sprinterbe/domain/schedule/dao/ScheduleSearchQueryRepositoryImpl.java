package com.nl.sprinterbe.domain.schedule.dao;

import com.nl.sprinterbe.domain.schedule.dto.QScheduleSearchResponse;
import com.nl.sprinterbe.domain.schedule.dto.ScheduleSearchResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nl.sprinterbe.domain.project.entity.QProject.project;
import static com.nl.sprinterbe.domain.schedule.entity.QSchedule.schedule;

@Repository
@RequiredArgsConstructor
public class ScheduleSearchQueryRepositoryImpl implements ScheduleSearchQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public List<ScheduleSearchResponse> searchSchedule(String keyword, Long projectId) {
        List<ScheduleSearchResponse> scheduleSearchResponses = query
                .select(
                        new QScheduleSearchResponse(
                                project.projectId.as("projectId"),
                                schedule.scheduleId.as("scheduleId"),
                                schedule.title.as("title")
                        )
                )
                .from(schedule)
                .innerJoin(schedule.project, project)
                .where(
                        containsKeyword(keyword),
                        eqProjectId(projectId)
                )
                .fetch();
        return scheduleSearchResponses;
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword == null || keyword.isEmpty()
                ? null
                : schedule.title.contains(keyword);
    }

    private BooleanExpression eqProjectId(Long projectId) {
        return projectId == null
                ? null
                : project.projectId.eq(projectId);
    }
}
