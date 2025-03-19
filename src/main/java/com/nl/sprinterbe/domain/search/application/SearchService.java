package com.nl.sprinterbe.domain.search.application;

import com.nl.sprinterbe.domain.search.dto.SearchResponseDto;

import java.util.List;

public interface SearchService {
    List<SearchResponseDto> search(String keyword);
}
