package com.nl.sprinterbe.domain.user.dto;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {

    private final Map<String,Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }
    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getNickname() {
        return attribute.get("name").toString();
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.toString();
    }
}
