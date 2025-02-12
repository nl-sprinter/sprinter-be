package com.nl.sprinterbe.handler;

import com.nl.sprinterbe.common.ResponseStatus;
import com.nl.sprinterbe.dto.CustomOAuth2User;
import com.nl.sprinterbe.entity.RefreshToken;
import com.nl.sprinterbe.repository.RefreshTokenRepository;
import com.nl.sprinterbe.service.RefreshTokenService;
import com.nl.sprinterbe.util.JwtUtil;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.nl.sprinterbe.dto.ResponseDto;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

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