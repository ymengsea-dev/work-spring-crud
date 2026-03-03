package com.example.productcrud.exception;

import com.example.productcrud.constraint.ErrorCode;
import com.example.productcrud.model.reponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ProductNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(ErrorCode.PRODUCT_NOT_FOUND.toString(),exception.getMessage(), 404, Instant.now())
        );
    }



}
