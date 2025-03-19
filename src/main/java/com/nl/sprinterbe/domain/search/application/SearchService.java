package com.nl.sprinterbe.domain.search.application;

import com.nl.sprinterbe.domain.search.dto.AllSearchResponseDto;

import java.util.List;

public interface SearchService {
    List<AllSearchResponseDto> search(String keyword);
}
