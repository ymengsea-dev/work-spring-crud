package com.example.productcrud.exception;

import com.example.productcrud.constraint.ExceptionCode;
import com.example.productcrud.model.dto.reponse.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // handle business exception
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException ex){
        ExceptionCode exceptionCode = ex.getExceptionCode();

        ApiResponse<?> body = ApiResponse.builder()
                .status(ApiStatus.builder()
                        .code(exceptionCode.toString())
                        .message(exceptionCode.getMessage())
                        .build())
                .data(ex.getData())
                .build();
        return ResponseEntity.status(exceptionCode.getCode()).body(body);
    }

    // handle spring validation error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ValidationErrorResponse>> handleValidationException(MethodArgumentNotValidException ex){

        // get message from the error field
        List <FieldError> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map( e ->
                    FieldError.builder()
                            .field(e.getField())
                            .message(e.getDefaultMessage())
                            .build()
                ).toList();

        ApiResponse<ValidationErrorResponse> body = ApiResponse.<ValidationErrorResponse>builder()
                .status(ApiStatus.builder()
                        .code(ExceptionCode.VALIDATION_ERROR.name()) // static bad request because validation
                        .message(ExceptionCode.VALIDATION_ERROR.getMessage())
                        .build())
                .data(ValidationErrorResponse.builder()
                        .errors(errors)
                        .build())
                .build();

        return ResponseEntity.status(400).body(body);
    }

    // catch all fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex){
        ex.printStackTrace();
        ApiResponse<?> body = ApiResponse.builder()
                .status(ApiStatus.builder()
                        .code(ExceptionCode.INTERNAL_SERVER_ERROR.name())
                        .message(ExceptionCode.INTERNAL_SERVER_ERROR.getMessage())
                        .build())
                .build();
        return ResponseEntity.status(500).body(body);
    }
}
