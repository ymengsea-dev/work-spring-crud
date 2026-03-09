package com.example.productcrud.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // Product
    PRODUCT_RETRIEVED("SUCCESS",  200, "Product retrieved successfully."),
    PRODUCT_CREATED("CREATED",    201, "Product created successfully."),
    PRODUCT_UPDATED("SUCCESS",    200, "Product updated successfully."),
    PRODUCT_DELETED("DELETED",    200, "Product deleted successfully."),

    // User
    USER_CREATED("CREATED",       201, "User created successfully."),

    // Auth
    LOGIN_SUCCESS("SUCCESS",      200, "Login successful."),
    LOGOUT_SUCCESS("SUCCESS",     200, "Logout successful."),
    TOKEN_REFRESHED("SUCCESS",    200, "Token refreshed successfully.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
