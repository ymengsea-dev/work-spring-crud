package com.example.productcrud.model.reponse;

import com.example.productcrud.constraint.Currency;
import com.example.productcrud.constraint.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String code;
    private String name;
    private Double price;
    private Currency currency;
    private ProductStatus status;
}
