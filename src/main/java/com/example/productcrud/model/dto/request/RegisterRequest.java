package com.example.productcrud.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @NotBlank
    private  String username;

    @Email
    @NotBlank(message = "Email is required")
    private  String email;

    @NotBlank(message = "Password is required")
    @Size(min = 4 , max = 16)
    private String password;
}
