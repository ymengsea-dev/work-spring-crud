package com.example.productcrud.config;

import com.example.productcrud.model.CustomOAuth2User;
import com.example.productcrud.model.CustomUserDetail;
import com.example.productcrud.model.User;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();


        // generate token
        String token = jwtService.generateToken(new CustomUserDetail(user));

        response.setContentType("application/json");
        response.getWriter().write("""
            {
              "token": "%s"
            }
        """.formatted(token));
    }
}
