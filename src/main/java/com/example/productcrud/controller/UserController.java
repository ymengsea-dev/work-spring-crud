package com.example.productcrud.controller;

import com.example.productcrud.constraint.ErrorCode;
import com.example.productcrud.model.User;
import com.example.productcrud.model.dto.reponse.ApiResponse;
import com.example.productcrud.model.dto.reponse.ApiStatus;
import com.example.productcrud.model.dto.reponse.AuthResponse;
import com.example.productcrud.model.dto.reponse.UserResponse;
import com.example.productcrud.model.dto.request.LoginRequest;
import com.example.productcrud.model.dto.request.RegisterRequest;
import com.example.productcrud.service.UserService;
import com.example.productcrud.service.impl.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.SUCCESS.toString())
                        .message("Registration successful.")
                        .build())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        ApiResponse<AuthResponse> body = ApiResponse.<AuthResponse>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.LOGIN_SUCCESS.toString())
                        .message("Login successful.")
                        .build())
                .data(authResponse)
                .build();
        return ResponseEntity.ok(body);
    }
}
