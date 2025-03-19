package com.nl.sprinterbe.domain.search.api;

import com.nl.sprinterbe.domain.search.application.SearchService;
import com.nl.sprinterbe.domain.search.dto.SearchResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "검색 기능", description = "이슈, 백로그, task, 스케줄, 데일리스크럼 본문을 검색합니다.")
    @GetMapping("/test")
    public ResponseEntity<List<SearchResponseDto>> search(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(searchService.search(keyword));
    }
}
