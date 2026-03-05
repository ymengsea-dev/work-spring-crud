package com.example.productcrud.model.dto.request;

import com.example.productcrud.constraint.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateProductRequest {

    private String code;

    private String name;

    private String description;

    private Double price;

    private CurrencyCode currency;
}

