package com.nl.sprinterbe.domain.task.dao;

import com.nl.sprinterbe.domain.task.dto.QTaskSearchResponse;
import com.nl.sprinterbe.domain.task.dto.TaskSearchResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nl.sprinterbe.domain.backlog.entity.QBacklog.backlog;
import static com.nl.sprinterbe.domain.project.entity.QProject.project;
import static com.nl.sprinterbe.domain.sprint.entity.QSprint.sprint;
import static com.nl.sprinterbe.domain.task.entity.QTask.task;

@Repository
@RequiredArgsConstructor
public class TaskSearchQueryRepositoryImpl implements TaskSearchQueryRepository {
    private final JPAQueryFactory query;


    @Override
    public List<TaskSearchResponse> searchTask(String keyword, Long projectId) {
        List<TaskSearchResponse> taskSearchResponses = query
                .select(
                        new QTaskSearchResponse(
                                task.content.as("content"),
                                project.projectId.as("projectId"),
                                sprint.sprintId.as("sprintId"),
                                backlog.backlogId.as("backlogId")
                        )
                )
                .from(task)
                .innerJoin(task.backlog, backlog)
                .innerJoin(backlog.sprint, sprint)
                .innerJoin(sprint.project, project)
                .where(
                        containsKeyword(keyword),
                        eqProjectId(projectId)
                )
                .fetch();

        return taskSearchResponses;
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword == null || keyword.isEmpty()
                ? null
                : task.content.contains(keyword);
    }

    private BooleanExpression eqProjectId(Long projectId) {
        return projectId == null
                ? null
                : project.projectId.eq(projectId);
    }
}
