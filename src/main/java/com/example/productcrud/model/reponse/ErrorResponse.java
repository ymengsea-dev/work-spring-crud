package com.example.productcrud.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
    private int Status;
    private Instant timestamp;
}
