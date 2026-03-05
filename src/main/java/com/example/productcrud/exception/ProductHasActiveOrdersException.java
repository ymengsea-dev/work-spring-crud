package com.example.productcrud.exception;

public class ProductHasActiveOrdersException extends RuntimeException {

    public ProductHasActiveOrdersException(String message) {
        super(message);
    }
}

