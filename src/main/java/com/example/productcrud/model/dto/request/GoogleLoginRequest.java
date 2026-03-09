package com.example.productcrud.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequest {
    @Email
    @NotBlank(message = "Email must not be blank")
    @Schema(description = "your email", example = "mengsea@gmail.com")
    private String email;
}
