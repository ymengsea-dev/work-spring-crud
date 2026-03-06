package com.example.productcrud.service;

import com.example.productcrud.model.dto.reponse.AuthResponse;

public interface UserService {
    void register(String username, String email, String password);
    AuthResponse login(String email, String password);
}
