package com.nl.sprinterbe.domain.search.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {

    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::: Search ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */

    @Operation(summary = "검색 기능", description = "이슈, 백로그, task, 스케줄, 데일리스크럼 본문을 검색합니다.")
    @GetMapping("/")
    public void search(@RequestParam(required = false) String keyword) {
    }
}
