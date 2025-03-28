package com.nl.sprinterbe.domain.todo.api;

import com.nl.sprinterbe.domain.todo.application.TodoService;
import com.nl.sprinterbe.domain.todo.dto.TodoCountResponse;
import com.nl.sprinterbe.domain.todo.dto.TodoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/todo")
    public ResponseEntity<List<TodoResponse>> getTodoList() {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodos());
    }

    @GetMapping("/todo/count")
    public ResponseEntity<Integer> getTodoCount() {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodoCount());
    }



}
