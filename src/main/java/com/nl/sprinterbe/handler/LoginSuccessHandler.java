package com.nl.sprinterbe.handler;

import com.nl.sprinterbe.common.ResponseStatus;
import com.nl.sprinterbe.dto.CustomUserDetails;
import com.nl.sprinterbe.dto.ResponseDto;
import com.nl.sprinterbe.entity.RefreshToken;
import com.nl.sprinterbe.repository.RefreshTokenRepository;
import com.nl.sprinterbe.service.RefreshTokenService;
import com.nl.sprinterbe.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

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

        ResponseDto.settingResponse(response, HttpStatus.OK, ResponseStatus.LOCAL_LOGIN_SUCCESS,accessToken,refreshToken);

    }
}
