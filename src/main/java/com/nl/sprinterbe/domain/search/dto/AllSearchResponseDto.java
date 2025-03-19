package com.nl.sprinterbe.domain.search.dto;

import com.nl.sprinterbe.domain.search.type.SearchType;
import com.querydsl.core.types.dsl.StringExpression;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllSearchResponseDto {
    private SearchType searchType;
    private String title;
    private String content;
    private String url;

    public AllSearchResponseDto(StringExpression title) {
    }


    public static AllSearchResponseDto of(SearchType searchType, String title, String content, String url) {
        return AllSearchResponseDto.builder()
                .searchType(searchType)
                .title(title)
                .content(content)
                .url(url)
                .build();
    }
}
