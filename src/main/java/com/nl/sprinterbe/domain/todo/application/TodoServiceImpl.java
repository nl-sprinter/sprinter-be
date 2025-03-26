package com.nl.sprinterbe.domain.todo.application;

import com.nl.sprinterbe.domain.schedule.dao.ScheduleRepository;
import com.nl.sprinterbe.domain.task.dao.TaskRepository;
import com.nl.sprinterbe.domain.todo.dao.TodoRepository;
import com.nl.sprinterbe.domain.todo.dto.TodoResponse;
import com.nl.sprinterbe.domain.todo.entity.TodoType;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
import com.nl.sprinterbe.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {

    private final TaskRepository taskRepository;
    private final SecurityUtil securityUtil;
    private final ScheduleRepository scheduleRepository;

    @Override
    public List<TodoResponse> getTodos() {
        long userId = getUserId();
        List<TodoResponse> responses = taskRepository.findUncheckedTasksByUserId(userId);
        for (TodoResponse t : responses) {
            t.setTodoType(TodoType.TASK);
        }
        List<TodoResponse> schedulesResponses = scheduleRepository.findSchedulesByUserIdAndDate(userId, LocalDateTime.now());
        for (TodoResponse s : schedulesResponses) {
            s.setTodoType(TodoType.SCHEDULE);
        }
        responses.addAll(schedulesResponses);

        for (TodoResponse r : responses) {
            System.out.println(r);
        }
        return responses;
    }

    @Override
    public int getTodoCount() {
        int count = taskRepository.countTasksTodoByUserIdAndUnchecked(getUserId());
        count += scheduleRepository.countSchedulesTodoByUserIdAndDate(getUserId(), LocalDateTime.now());
        return count;
    }

    private Long getUserId() {
        return Long.parseLong(securityUtil.getCurrentUserId().orElseThrow(UserNotFoundException::new));
    }



}
