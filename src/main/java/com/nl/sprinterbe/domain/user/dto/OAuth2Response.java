package com.nl.sprinterbe.domain.user.dto;


public interface OAuth2Response {
    String getEmail();
    String getNickname();
    String getProvider();
    String getProviderId();
}
