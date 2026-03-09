package com.example.productcrud.model.dto.request;

import com.example.productcrud.constraint.CurrencyCode;
import com.example.productcrud.constraint.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "product code is required")
    private String code;

    @NotBlank(message = "product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be greater than 0.")
    private Double price;

    @NotNull(message = "Currency code is required.")
    private CurrencyCode currency;

    private ProductStatus status;

}
