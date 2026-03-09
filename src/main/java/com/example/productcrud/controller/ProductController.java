package com.example.productcrud.controller;

import com.example.productcrud.constraint.ProductStatus;
import com.example.productcrud.constraint.ResponseCode;
import com.example.productcrud.model.dto.reponse.ApiResponse;
import com.example.productcrud.model.dto.reponse.ApiStatus;
import com.example.productcrud.model.dto.reponse.PagedResponse;
import com.example.productcrud.model.dto.reponse.ProductResponse;
import com.example.productcrud.model.dto.request.ProductPatchRequest;
import com.example.productcrud.model.dto.request.ProductRequest;
import com.example.productcrud.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Controller")
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all product")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> getProduct(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        PagedResponse<ProductResponse> data = productService.getProductPage(query, status, page, size, sortBy, sortDir);
        ApiResponse<PagedResponse<ProductResponse>> body = ApiResponse.<PagedResponse<ProductResponse>>builder()
                .status(ApiStatus.builder()
                        .code(ResponseCode.PRODUCT_RETRIEVED.name())
                        .message(ResponseCode.PRODUCT_RETRIEVED.getMessage())
                        .build())
                .data(data)
                .build();
        return ResponseEntity.ok(body);
    }

    // add product
    @Operation(summary = "Add new product")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse data = productService.addProductAndGetResponse(productRequest);
        ApiResponse<ProductResponse> body = ApiResponse.<ProductResponse>builder()
                .status(ApiStatus.builder()
                        .code(ResponseCode.PRODUCT_CREATED.name())
                        .message(ResponseCode.PRODUCT_CREATED.getMessage())
                        .build())
                .data(data)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // get product by ID
    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Integer id) {
        ProductResponse data = productService.getProductResponseById(id);
        ApiResponse<ProductResponse> body = ApiResponse.<ProductResponse>builder()
                .status(ApiStatus.builder()
                        .code(ResponseCode.PRODUCT_RETRIEVED.name())
                        .message(ResponseCode.PRODUCT_RETRIEVED.getMessage())
                        .build())
                .data(data)
                .build();
        return ResponseEntity.ok(body);
    }

    // update productBy ID
    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductRequest productRequest) {
        ProductResponse data = productService.updateProductAndGetResponse(id, productRequest);
        ApiResponse<ProductResponse> body = ApiResponse.<ProductResponse>builder()
                .status(ApiStatus.builder()
                        .code(ResponseCode.PRODUCT_UPDATED.name())
                        .message(ResponseCode.PRODUCT_UPDATED.getMessage())
                        .build())
                .data(data)
                .build();
        return ResponseEntity.ok(body);
    }

    // patch product
    @Operation(summary = "Update product field")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> patchProduct(
            @PathVariable Integer id,
            @RequestBody ProductPatchRequest productPatchRequest) {
        ProductResponse data = productService.patchProduct(id, productPatchRequest);
        ApiResponse<ProductResponse> body = ApiResponse.<ProductResponse>builder()
                .status(ApiStatus.builder()
                        .code(ResponseCode.PRODUCT_UPDATED.name())
                        .message(ResponseCode.PRODUCT_UPDATED.getMessage())
                        .build())
                .data(data)
                .build();
        return ResponseEntity.ok(body);
    }

    // delete product
    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(ApiStatus.builder()
                        .code(ResponseCode.PRODUCT_DELETED.name())
                        .message(ResponseCode.PRODUCT_DELETED.getMessage())
                        .build())
                .data(null)
                .build();
        return ResponseEntity.ok(body);
    }
}
