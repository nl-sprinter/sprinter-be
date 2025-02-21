package com.nl.sprinterbe.dto;

import com.nl.sprinterbe.domain.user.dto.UserDetailResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserDetailResponse userDetailResponse;

    public CustomOAuth2User(UserDetailResponse userDetailResponse) {
        this.userDetailResponse = userDetailResponse;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDetailResponse.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return userDetailResponse.getUserId().toString();
    }

    public String getEmail() {
        return userDetailResponse.getEmail();
    }

    public String getId() {
        return userDetailResponse.getUserId().toString();
    }
}
