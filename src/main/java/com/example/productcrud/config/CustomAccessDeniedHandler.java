package com.example.productcrud.config;

import com.example.productcrud.constraint.ExceptionCode;
import com.example.productcrud.model.dto.reponse.ApiResponse;
import com.example.productcrud.model.dto.reponse.ApiStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    //  this class handle role permission, what use with this role can do // code 403
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ApiResponse<Object> body = ApiResponse.builder()
                .status(ApiStatus.builder()
                        .code(ExceptionCode.ACCESS_DENIED.name())
                        .message(ExceptionCode.ACCESS_DENIED.getMessage())
                        .build())
                .data(null)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), body);
    }
}

