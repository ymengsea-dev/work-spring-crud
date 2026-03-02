package com.example.productcrud.model.reponse;

import com.example.productcrud.model.User;

public class AuthResponse {
    private  String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private UserResponse user;
}
