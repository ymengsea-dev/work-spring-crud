package com.example.productcrud.controller;

import com.example.productcrud.constraint.ResponseCode;
import com.example.productcrud.model.dto.reponse.ApiResponse;
import com.example.productcrud.model.dto.reponse.ApiStatus;
import com.example.productcrud.model.dto.reponse.AuthResponse;
import com.example.productcrud.model.dto.reponse.RefreshTokenResponse;
import com.example.productcrud.model.dto.request.LoginRequest;
import com.example.productcrud.model.dto.request.RefreshTokenRequest;
import com.example.productcrud.model.dto.request.RegisterRequest;
import com.example.productcrud.service.RefreshTokenService;
import com.example.productcrud.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Authentication")
@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register User")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(ApiStatus.builder()
                        .code(ResponseCode.USER_CREATED.name())
                        .message(ResponseCode.USER_CREATED.getMessage())
                        .build())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        ApiResponse<AuthResponse> body = ApiResponse.<AuthResponse>builder()
                .status(ApiStatus.builder()
                        .code(ResponseCode.LOGIN_SUCCESS.name())
                        .message(ResponseCode.LOGIN_SUCCESS.getMessage())
                        .build())
                .data(authResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Operation(summary = "refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshTokenResponse response = userService.refreshToken(refreshTokenRequest);
        ApiResponse<RefreshTokenResponse> body = ApiResponse.<RefreshTokenResponse>builder()
                .status(ApiStatus.builder()
                        .code(ResponseCode.TOKEN_REFRESHED.name())
                        .message(ResponseCode.TOKEN_REFRESHED.getMessage())
                        .build())
                .data(response)
                .build();
       return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @Operation(summary = "Login with google")
    @GetMapping("/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }
}
