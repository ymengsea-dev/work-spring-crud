package com.example.productcrud.exception;

import com.example.productcrud.constraint.ErrorCode;
import com.example.productcrud.model.reponse.AccountLockedData;
import com.example.productcrud.model.reponse.ApiResponse;
import com.example.productcrud.model.reponse.ApiStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
