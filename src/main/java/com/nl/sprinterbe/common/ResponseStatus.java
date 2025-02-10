package com.nl.sprinterbe.common;

public enum ResponseStatus {
    A_TOKEN_EXPIRED("Access Token Expired", "ACCESS_TOKEN_EXPIRED"),
    R_TOKEN_EXPIRED("Refresh Token Expired", "REFRESH_TOKEN_EXPIRED"),
    LOCAL_LOGIN_FAILED("Invalid Email or Password", "CREDENTIALS_INVALID"),
    LOGIN_FORM_INVALID("Invalid Login Form", "LOGIN_FORM_INVALID"),
    OAUTH_LOGIN_FAILED("Invalid Oauth Processing", "OAUTH_INVALID"),
    OAUTH_LOGIN_SUCCESS("Success Oauth Login", "OAUTH_SUCCESS"),
    LOCAL_LOGIN_SUCCESS("Success Local Login", "LOGIN_SUCCESS"),
    AUTHORIZATION_FAILED("Authorization Failed", "AUTHORIZATION_FAILED"),
    TOKEN_INVALID("Invalid Token", "Token_INVALID"),
    DB_ERROR("DB Error", "DB_ERROR"),
    JSON_FORM_ERROR("Json Form Error", "JSON_FORM_ERROR"),
    TOKEN_CREATED("Access,Refresh Token Created","TOKEN_CREATED"),
    LOGOUT_SUCCESS("Logout Success","LOGOUT_SUCCESS"),
    LOGOUT_FAILED("Logout Failed","LOGOUT_FAILED"),
    NO_RESOURCE_FOUND("Resource Found Failed","NO_RESOURCE_FOUND"),
    //핸들러 없음 , Http Method 불일치 등등 Dispatcher Servlet 관련 전반 에러
    SERVLET_ERROR("Servlet Error","SERVLET_ERROR"),
    TOKEN_COMI_ERROR("Token Combination Error","TOKEN_COMBI_ERROR"),
    EMAIL_DUPLICATE("Duplicate Email","EMAIL_DUPLICATED");

    private final String message;
    private final String code;

    ResponseStatus(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

}
