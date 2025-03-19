package com.nl.sprinterbe.domain.dailyscrum.dao;

import com.nl.sprinterbe.domain.dailyscrum.dto.DailyScrumSearchResponse;

import java.util.List;

public interface DailyScrumSearchQueryRepository {
    List<DailyScrumSearchResponse> searchDailyScrum(String keyword);
}
