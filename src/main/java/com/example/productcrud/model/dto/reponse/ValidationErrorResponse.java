package com.example.productcrud.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ValidationErrorResponse {
    private List<FieldError> errors;
}
