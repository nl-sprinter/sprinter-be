package com.nl.sprinterbe.service;

import com.nl.sprinterbe.domain.user.dto.GoogleResponse;
import com.nl.sprinterbe.domain.user.dto.OAuth2Response;
import com.nl.sprinterbe.domain.user.dto.UserDetailResponse;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.nl.sprinterbe.dto.CustomOAuth2User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if(registrationId.equalsIgnoreCase("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
            System.out.println("oAuth2Response.getNickname() = " + oAuth2Response.getNickname());
        }

        String providerName = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        User existingUser = userRepository.findByEmail(oAuth2Response.getEmail()).orElse(null);

        if(existingUser == null) {

            User user = User.builder()
                    .email(oAuth2Response.getEmail())
                    .nickname(oAuth2Response.getNickname())
                    .build();

            userRepository.save(user);

            UserDetailResponse userDetailResponse = new UserDetailResponse();
            userDetailResponse.setEmail(oAuth2Response.getEmail());
            userDetailResponse.setUserId(user.getUserId());
            userDetailResponse.setNickname(oAuth2Response.getNickname());
            //userDTO.setProviderName(providerName);
            userDetailResponse.setRole("ROLE_USER");

            return new CustomOAuth2User(userDetailResponse);
        }
        else {

            existingUser.setNickname(oAuth2Response.getNickname());
            existingUser.setEmail(oAuth2Response.getEmail());

            userRepository.save(existingUser);

            UserDetailResponse userDetailResponse = new UserDetailResponse();
            userDetailResponse.setEmail(oAuth2Response.getEmail());
            userDetailResponse.setNickname(oAuth2Response.getNickname());
            userDetailResponse.setUserId(existingUser.getUserId());
            //userDTO.setProviderName(providerName);
            userDetailResponse.setRole("ROLE_USER");

            return new CustomOAuth2User(userDetailResponse);
        }

    }
}
