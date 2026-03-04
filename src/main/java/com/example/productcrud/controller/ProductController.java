package com.example.productcrud.controller;

import com.example.productcrud.constraint.ErrorCode;
import com.example.productcrud.model.reponse.ApiResponse;
import com.example.productcrud.model.reponse.ApiStatus;
import com.example.productcrud.model.reponse.PagedResponse;
import com.example.productcrud.model.reponse.ProductResponse;
import com.example.productcrud.model.request.ProductRequest;
import com.example.productcrud.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    // get product
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> getProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponse<ProductResponse> data = productService.getProductPage(pageable);
        ApiResponse<PagedResponse<ProductResponse>> body = ApiResponse.<PagedResponse<ProductResponse>>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.SUCCESS.toString())
                        .message("Product list retrieved successfully.")
                        .build())
                .data(data)
                .build();
        return ResponseEntity.ok(body);
    }

    // add product
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse data = productService.addProductAndGetResponse(productRequest);
        ApiResponse<ProductResponse> body = ApiResponse.<ProductResponse>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.SUCCESS.toString())
                        .message("Product created successfully.")
                        .build())
                .data(data)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Integer id) {
        ProductResponse data = productService.getProductResponseById(id);
        ApiResponse<ProductResponse> body = ApiResponse.<ProductResponse>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.SUCCESS.toString())
                        .message("Product retrieved successfully.")
                        .build())
                .data(data)
                .build();
        return ResponseEntity.ok(body);
    }

    // update productBy ID
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductRequest productRequest) {
        ProductResponse data = productService.updateProductAndGetResponse(id, productRequest);
        ApiResponse<ProductResponse> body = ApiResponse.<ProductResponse>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.SUCCESS.toString())
                        .message("Product updated successfully.")
                        .build())
                .data(data)
                .build();
        return ResponseEntity.ok(body);
    }

    // delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(ApiStatus.builder()
                        .code(ErrorCode.SUCCESS.toString())
                        .message("Product deleted successfully.")
                        .build())
                .data(null)
                .build();
        return ResponseEntity.ok(body);
    }
}
