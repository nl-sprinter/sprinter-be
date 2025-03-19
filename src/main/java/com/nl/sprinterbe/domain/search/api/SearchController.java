package com.nl.sprinterbe.domain.search.api;

import com.nl.sprinterbe.domain.search.application.SearchService;
import com.nl.sprinterbe.domain.search.dto.SearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {

    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Search ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    private final SearchService searchService;

    @Operation(summary = "검색 기능", description = "ISSUE, TASK, BACKLOG, SCHEDULE, DAILYSCRUM을 검색합니다.")
    @GetMapping("/test")
    public ResponseEntity<List<SearchResponse>> search(@RequestParam(required = false) String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.search(keyword));
    }
}
