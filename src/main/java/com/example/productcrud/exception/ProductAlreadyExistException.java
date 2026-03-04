package com.example.productcrud.exception;

import lombok.Data;

@Data
public class ProductAlreadyExistException extends RuntimeException{
    private String productCode;

    public ProductAlreadyExistException(String message, String productCodee){
        super(message);
        this.productCode = productCodee;
    }
}
