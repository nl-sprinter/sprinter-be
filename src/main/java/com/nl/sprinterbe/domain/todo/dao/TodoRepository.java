package com.nl.sprinterbe.domain.todo.dao;

import com.nl.sprinterbe.domain.todo.dto.TodoResponse;
import com.nl.sprinterbe.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {


}
