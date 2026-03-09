package com.example.productcrud.exception;

import com.example.productcrud.constraint.ExceptionCode;

public class ProductException extends BusinessException{
    public ProductException(ExceptionCode exceptionCode){
        super(exceptionCode);
    }
    public ProductException(ExceptionCode exceptionCode, Object data) {
        super(exceptionCode, data);
    }
}
