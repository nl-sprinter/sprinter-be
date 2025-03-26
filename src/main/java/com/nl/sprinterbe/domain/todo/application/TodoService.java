package com.nl.sprinterbe.domain.todo.application;

import com.nl.sprinterbe.domain.todo.dto.TodoResponse;

import java.util.List;

public interface TodoService {

    List<TodoResponse> getTodos();

    int getTodoCount();
}
