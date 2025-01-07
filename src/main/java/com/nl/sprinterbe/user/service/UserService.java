package com.nl.sprinterbe.user.service;

import com.nl.sprinterbe.user.dto.UserDTO;
import com.nl.sprinterbe.user.entity.User;
import com.nl.sprinterbe.user.repository.UserRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void registerUser(UserDTO userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + userDTO.getEmail() + " already exists");
        }

        User user = User.builder()
                .nickName(userDTO.getNickName())
                .email(userDTO.getEmail())
                .password(encodedPassword)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        //시큐리티context에 있는 유저정보를 업데이트

        user.setNickName(userDTO.getNickName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);
    }

    @Transactional
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDTO.builder()
                        .userId(user.getUserId())
                        .nickName(user.getNickName())
                        .email(user.getEmail())
                        .build())
                .collect(Collectors.toList());
    }

}
