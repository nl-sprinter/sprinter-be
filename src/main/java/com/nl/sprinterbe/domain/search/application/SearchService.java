package com.nl.sprinterbe.domain.search.application;

import com.nl.sprinterbe.domain.search.dto.SearchResponse;

import java.util.List;

public interface SearchService {
    List<SearchResponse> search(String keyword, Long projectId);
}
