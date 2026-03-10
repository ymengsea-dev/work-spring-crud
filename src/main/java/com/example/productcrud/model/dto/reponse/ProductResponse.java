package com.example.productcrud.model.dto.reponse;

import com.example.productcrud.constraint.CurrencyCode;
import com.example.productcrud.constraint.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse implements Serializable {
    private String id;
    private String code;
    private String name;
    private String description;
    private Double price;
    private CurrencyCode currency;
    private ProductStatus status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
