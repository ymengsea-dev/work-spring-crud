package com.example.productcrud.service.impl;

import com.example.productcrud.constraint.AuthProvider;
import com.example.productcrud.constraint.Role;
import com.example.productcrud.model.CustomOAuth2User;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found");
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createGoogleUser(email,name));

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private User createGoogleUser(String email, String name) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(null);
        user.setEmail(email);
        user.setRoles(new ArrayList<>(List.of(Role.PRODUCT_READ.toString())));
        user.setAuthProvider(AuthProvider.GOOGLE);

        return userRepository.save(user);
    }

}
