package com.example.productcrud.config;

import com.example.productcrud.constraint.ExceptionCode;
import com.example.productcrud.model.dto.reponse.ApiResponse;
import com.example.productcrud.model.dto.reponse.ApiStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                .status(ApiStatus.builder()
                        .code(ExceptionCode.OAUTH2_LOGIN_FAILED.name())
                        .message(ExceptionCode.OAUTH2_LOGIN_FAILED.getMessage())
                        .build())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}
