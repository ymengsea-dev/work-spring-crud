package com.example.productcrud.model.request;

import com.example.productcrud.constraint.CurrencyCode;
import com.example.productcrud.constraint.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Currency;

@Data
@Builder
@AllArgsConstructor
public class ProductRequest {

    private String code;

    private String name;

    private String description;

    private Double price;

    private CurrencyCode currency;

    private ProductStatus status;

}
