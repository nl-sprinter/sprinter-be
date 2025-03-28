package com.nl.sprinterbe.global.security.handler.oauth2;

import com.nl.sprinterbe.dto.CustomOAuth2User;
import com.nl.sprinterbe.domain.refreshtoken.application.RefreshTokenService;
import com.nl.sprinterbe.global.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler{
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2UserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String id = oAuth2UserDetails.getId();

        String refreshToken = jwtUtil.createRefreshJwt(id);

        refreshTokenService.updateExpiredTokens(id);
        refreshTokenService.addRefresh(id, refreshToken);

        settingResponse(response, HttpStatus.OK,null, refreshToken);
        response.sendRedirect("http://localhost:3000/refresh");
    }

    //필터단: data 필드 없는 Json
    public static void settingResponse(HttpServletResponse response, HttpStatus httpStatus, String accessToken , String refreshToken) {
        // 응답 헤더 및 쿠키 설정
        if (accessToken != null) {
            response.addHeader("Authorization", "Bearer " + accessToken);
        }
        response.setHeader("Set-Cookie","Refresh="+refreshToken+"; Path=/; Max-Age=259200; HttpOnly");
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
    }
}