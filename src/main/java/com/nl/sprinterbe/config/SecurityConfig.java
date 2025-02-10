package com.nl.sprinterbe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.sprinterbe.filter.CustomUsernamePasswordAuthenticationFilter;
import com.nl.sprinterbe.filter.JwtFilter;
import com.nl.sprinterbe.handler.LoginFailureHandler;
import com.nl.sprinterbe.handler.LoginSuccessHandler;
import com.nl.sprinterbe.service.CustomOAuth2UserService;
import com.nl.sprinterbe.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug =true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService loginService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .addFilterBefore(jsonUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, CustomUsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/logout", "/login").permitAll()
                        .requestMatchers("/api/v1/auth/signup", "/login").permitAll()
                        .requestMatchers("/h2-console/**","/api/v1/auth/login", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .formLogin(form -> form
//                        .loginProcessingUrl("/api/v1/login") // 로그인 URL
//                        .usernameParameter("email")
//                        .passwordParameter("password")
//                        .successHandler((request, response, authentication) -> {
//                            response.setStatus(200);
//                            response.getWriter().write("Login successful");
//                        })
//                        .failureHandler((request, response, exception) -> {
//                            response.setStatus(401);
//                            response.getWriter().write("Login failed: " + exception.getMessage());
//                        })
//                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.getWriter().write("OAuth2 Login successful");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                            response.getWriter().write("OAuth2 Login failed: " + exception.getMessage());
                        })
                );
//                .sessionManagement(session -> session
////                        .invalidSessionUrl("/login")
//                        .sessionFixation().changeSessionId()
//                        .maximumSessions(1)
//                        .maxSessionsPreventsLogin(true)
//                        .expiredUrl("/login")
//                )
//                .logout(log -> log
//                        .logoutUrl("/api/v1/auth/logout")
//                        .logoutSuccessHandler((request, response, authentication) -> {
//                            response.setStatus(200);
//                            response.getWriter().write("Logout successful");
//                        })
//                        .deleteCookies("JSESSIONID")
//                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

    @Bean
    public CustomUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
        usernamePasswordAuthenticationFilter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        return usernamePasswordAuthenticationFilter;
    }

//    @Bean
//    public LoginSuccessHandler loginSuccessHandler() {
//        return new LoginSuccessHandler();
//    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);

        return new ProviderManager(provider);
    }

}
