package com.example.productcrud.exception;

import com.example.productcrud.constraint.ExceptionCode;

public class UserException extends BusinessException{
    public UserException(ExceptionCode exceptionCode){
        super(exceptionCode);
    }
}
