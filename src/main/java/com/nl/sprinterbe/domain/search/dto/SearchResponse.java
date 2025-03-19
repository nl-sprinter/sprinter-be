package com.nl.sprinterbe.domain.search.dto;

import com.nl.sprinterbe.domain.search.type.SearchType;
import com.querydsl.core.types.dsl.StringExpression;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse {
    private SearchType searchType;
    private String title;
    private String content;
    private String url;

    public SearchResponse(StringExpression title) {
    }


    public static SearchResponse of(SearchType searchType, String title, String content, String url) {
        return SearchResponse.builder()
                .searchType(searchType)
                .title(title)
                .content(content)
                .url(url)
                .build();
    }
}
