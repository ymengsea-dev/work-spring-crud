package com.example.productcrud.model.dto.request;

import com.example.productcrud.constraint.CurrencyCode;
import com.example.productcrud.constraint.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "ProductRequest", description = "Data required to create a product")
public class ProductRequest {

    @NotBlank(message = "product code is required")
    @Schema(description = "product code", example = "prd_23xm")
    private String code;

    @NotBlank(message = "product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be greater than 0.")
    @Schema(description = "Price", example = "25.99")
    private Double price;

    @NotNull(message = "Currency code is required.")
    @Schema(description = "IOS currency code", example = "USD, AUD, KHR...")
    private CurrencyCode currency;

    @Schema(description = "Status of product", example = "ACTIVE, INACTIVE")
    private ProductStatus status;

}
