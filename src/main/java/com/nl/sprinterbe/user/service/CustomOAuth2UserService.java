package com.nl.sprinterbe.user.service;

import com.nl.sprinterbe.user.dto.GoogleResponse;
import com.nl.sprinterbe.user.dto.OAuth2Response;
import com.nl.sprinterbe.user.dto.UserDTO;
import com.nl.sprinterbe.user.entity.User;
import com.nl.sprinterbe.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.nl.sprinterbe.user.dto.CustomOAuth2User;

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
        }

        String providerName = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        User existingUser = userRepository.findByEmail(oAuth2Response.getEmail()).orElse(null);

        if(existingUser == null) {

            User user = User.builder()
                    .email(oAuth2Response.getEmail())
                    .nickName(oAuth2Response.getNickName())
                    .build();

            userRepository.save(user);

            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setNickName(oAuth2Response.getNickName());
            //userDTO.setProviderName(providerName);
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
        else {

            existingUser.setNickName(oAuth2Response.getNickName());
            existingUser.setEmail(oAuth2Response.getEmail());

            userRepository.save(existingUser);

            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setNickName(oAuth2Response.getNickName());
            //userDTO.setProviderName(providerName);
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }

    }
}
