package com.example.productcrud.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthResponse {
    private  String accessToken;
    private String tokenType;
    private Date expiresIn;
    private UserResponse user;
}
