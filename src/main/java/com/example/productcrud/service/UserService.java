package com.example.productcrud.service;

import com.example.productcrud.model.dto.reponse.AuthResponse;
import com.example.productcrud.model.dto.reponse.RefreshTokenResponse;
import com.example.productcrud.model.dto.request.RefreshTokenRequest;

public interface UserService {
    void register(String username, String email, String password);
    AuthResponse login(String email, String password);
    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
