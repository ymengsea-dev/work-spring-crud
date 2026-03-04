package com.example.productcrud.model.reponse;

import com.example.productcrud.constraint.CurrencyCode;
import com.example.productcrud.constraint.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String code;
    private String name;
    private Double price;
    private CurrencyCode currency;
    private ProductStatus status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
