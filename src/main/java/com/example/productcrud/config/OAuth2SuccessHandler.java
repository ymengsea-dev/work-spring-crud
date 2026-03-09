package com.example.productcrud.config;

import com.example.productcrud.model.CustomOAuth2User;
import com.example.productcrud.model.CustomUserDetail;
import com.example.productcrud.model.User;
import com.example.productcrud.model.dto.reponse.AuthResponse;
import com.example.productcrud.model.dto.reponse.UserResponse;
import com.example.productcrud.service.impl.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;

    @Value("${jwt.expiration}")
    private String expiredIn;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        String token = jwtService.generateToken(new CustomUserDetail(user));

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(Long.valueOf(expiredIn))
                .user(UserResponse.builder()
                        .userId(user.getId())
                        .userName(user.getUsername())
                        .roles(user.getRoles())
                        .build())
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("""
                {
                  "accessToken": "%s",
                  "tokenType": "%s",
                  "expiresIn": %d,
                  "user": {
                    "userId": %d,
                    "userName": "%s",
                    "roles": %s
                  }
                }
                """.formatted(
                authResponse.getAccessToken(),
                authResponse.getTokenType(),
                authResponse.getExpiresIn(),
                authResponse.getUser().getUserId(),
                authResponse.getUser().getUserName(),
                authResponse.getUser().getRoles().toString()
        ));
    }
}
