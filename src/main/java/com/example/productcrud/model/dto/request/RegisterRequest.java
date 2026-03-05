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
    @NotBlank
    private  String username;

    @Email
    @NotBlank
    private  String email;

    @NotBlank
    @Size(min = 4 , max = 16)
    private String password;
}
