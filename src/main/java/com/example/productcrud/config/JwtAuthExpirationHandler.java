package com.example.productcrud.config;

import com.example.productcrud.constraint.ExceptionCode;
import com.example.productcrud.model.dto.reponse.ApiResponse;
import com.example.productcrud.model.dto.reponse.ApiStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthExpirationHandler implements AuthenticationEntryPoint {
    // this class handle user with no token or token expired // code : 401
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ApiResponse<Object> body = ApiResponse.builder()
            .status(ApiStatus.builder()
                .code(ExceptionCode.UNAUTHORIZED.name())
                .message(ExceptionCode.UNAUTHORIZED.getMessage())
                .build())
            .data(null)
            .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), body);
    }
}

