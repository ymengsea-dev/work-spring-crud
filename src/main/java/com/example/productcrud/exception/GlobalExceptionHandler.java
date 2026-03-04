package com.example.productcrud.exception;

import com.example.productcrud.constraint.ErrorCode;
import com.example.productcrud.model.reponse.AccountLockedData;
import com.example.productcrud.model.reponse.ApiResponse;
import com.example.productcrud.model.reponse.ApiStatus;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFound(ProductNotFoundException exception) {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.PRODUCT_NOT_FOUND.toString())
                        .message(exception.getMessage())
                        .build())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentials(BadCredentialsException exception) {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.INVALID_CREDENTIALS.toString())
                        .message("Invalid username or password.")
                        .build())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ApiResponse<AccountLockedData>> handleAccountLocked(AccountLockedException exception) {
        AccountLockedData data = AccountLockedData.builder()
                .lockedUntil(exception.getLockedUntil())
                .build();
        ApiResponse<AccountLockedData> body = ApiResponse.<AccountLockedData>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.ACCOUNT_LOCKED.toString())
                        .message("Account is locked due to multiple failed login attempts.")
                        .build())
                .data(data)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccountInactive(AccountInactiveException exception) {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.ACCOUNT_INACTIVE.toString())
                        .message("Account is inactive. Please contact administrator.")
                        .build())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFound(UsernameNotFoundException exception) {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.INVALID_CREDENTIALS.toString())
                        .message("Invalid username or password.")
                        .build())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCurrency(HttpMessageNotReadableException ex) {
        if (ex.getMessage().contains("java.util.Currency")) {
            ApiResponse<Void> body = ApiResponse.<Void>builder()
                    .status(ApiStatus.builder()
                            .code(ErrorCode.INVALID_CURRENCY_FORMAT.toString())
                            .message("Invalid Currency Code. Please use a valid ISO 4217 code (e.g., USD, KHR).")
                            .build()
                    )
                    .data(null)
                    .build();
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
        return null;
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<ApiResponse<Object>> handleProductExit(ProductAlreadyExistException exception){
        ApiResponse<Object> body = ApiResponse.builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.DUPLICATE_PRODUCT_CODE.toString())
                        .message(exception.getMessage())
                        .build())
                .data(exception.getProductCode())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleExpiredToken(ExpiredJwtException ex) {
        ApiResponse<Object> body = ApiResponse.builder()
                .status(ApiStatus.builder()
                        .code("TOKEN_EXPIRED")
                        .message("Your session has expired. Please login again.")
                        .build())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

}
