package com.nl.sprinterbe.global.security.handler;

import com.nl.sprinterbe.dto.CustomUserDetails;
import com.nl.sprinterbe.domain.refreshtoken.application.RefreshTokenService;
import com.nl.sprinterbe.global.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails auth = (CustomUserDetails) authentication.getPrincipal();

        String id = auth.getUsername();
        String email = auth.getEmail();

        String accessToken = jwtUtil.createJwt(id, email);
        String refreshToken = jwtUtil.createRefreshJwt(id);

        refreshTokenService.updateExpiredTokens(id);
        refreshTokenService.addRefresh(id,refreshToken);

        settingResponse(response, HttpStatus.OK,accessToken,refreshToken);

    }

    //필터단: data 필드 없는 Json
    public static void settingResponse(HttpServletResponse response, HttpStatus httpStatus, String accessToken , String refreshToken) throws IOException {
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
