package com.nl.sprinterbe.domain.search.dto;

import com.nl.sprinterbe.domain.search.type.SearchType;
import com.querydsl.core.types.dsl.StringExpression;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponseDto {
    private SearchType searchType;
    private String title;
    private String content;
    private String url;

    public SearchResponseDto(StringExpression title) {
    }


    public static SearchResponseDto of(SearchType searchType, String title, String url) {
        return SearchResponseDto.builder()
                .searchType(searchType)
                .title(title)
                .content("")
                .url(url)
                .build();
    }
}
