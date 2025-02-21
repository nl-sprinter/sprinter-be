package com.nl.sprinterbe.global.security.handler.oauth2;

import com.nl.sprinterbe.global.common.code.ResponseStatus;
import com.nl.sprinterbe.dto.CustomOAuth2User;
import com.nl.sprinterbe.domain.refreshToken.application.RefreshTokenService;
import com.nl.sprinterbe.global.security.JwtUtil;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.nl.sprinterbe.global.common.ResponseDto;

import java.io.IOException;

@Component
@RequiredArgsConstructor
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

        ResponseDto.settingResponse(response,HttpStatus.OK, ResponseStatus.OAUTH_LOGIN_SUCCESS,null,refreshToken);
        response.sendRedirect("http://localhost:3000/refresh");
    }
}