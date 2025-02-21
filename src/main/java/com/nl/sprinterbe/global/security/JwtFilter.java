package com.nl.sprinterbe.global.security;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.nl.sprinterbe.global.common.code.ResponseStatus;
import com.nl.sprinterbe.dto.CustomUserDetails;
import com.nl.sprinterbe.global.common.ErrorDto;
import com.nl.sprinterbe.domain.user.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        String refreshToken = jwtUtil.getRefreshToken(request);

        if(isLoginRequest(authorization,refreshToken) || isRefreshAndLogoutRequest(authorization,refreshToken)){
            filterChain.doFilter(request, response);
            return;
        }
        else if(isAfterLoginRequest(authorization,refreshToken)){
            String accessToken = authorization.split(" ")[1];
            //Refresh Token이 없고, Access Token은 있음
            try {
                // Access Token 만료 검증
                if (!jwtUtil.isExpired(accessToken)) {
                    // 유효한 토큰 처리
                    String id = jwtUtil.getId(accessToken);
                    String email = jwtUtil.getEmail(accessToken);
                    String role = jwtUtil.getRole(accessToken);

                    User user = new User();
                    user.setUserId(Long.parseLong(id));
                    user.setEmail(email);
                    user.setPassword("temppassword");
//                    user.setRole(role);

                    CustomUserDetails customUserDetails = new CustomUserDetails(user);
                    Authentication authToken = new UsernamePasswordAuthenticationToken(
                            customUserDetails, null, customUserDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    filterChain.doFilter(request, response);
                }
            } catch (ExpiredJwtException ex){
                ErrorDto.settingResponse(response,HttpStatus.UNAUTHORIZED, ResponseStatus.A_TOKEN_EXPIRED);
                return;
            } catch (JwtException ex){
                ErrorDto.settingResponse(response,HttpStatus.BAD_REQUEST,ResponseStatus.TOKEN_INVALID);
                return;
            }
        }
        else {
            ErrorDto.settingResponse(response,HttpStatus.UNAUTHORIZED,ResponseStatus.TOKEN_COMI_ERROR);
            return;
        }


    }

    private boolean isLoginRequest(String authorization, String refreshToken){
        if(authorization==null && refreshToken==null){
            return true;
        }
        else {
            return false;
        }
    }
    private boolean isRefreshAndLogoutRequest(String authorization, String refreshToken){
        if(authorization==null && refreshToken!=null){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isLogout(String authorization, String refreshToken){
        if(authorization==null && refreshToken!=null){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isAfterLoginRequest(String authorization, String refreshToken){
        if(authorization!=null && refreshToken==null && authorization.startsWith("Bearer ")){
            return true;
        }
        else {
            return false;
        }
    }
}
