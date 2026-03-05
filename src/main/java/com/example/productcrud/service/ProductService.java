package com.example.productcrud.service;

import com.example.productcrud.constraint.ProductStatus;
import com.example.productcrud.model.Product;
import com.example.productcrud.model.dto.reponse.PagedResponse;
import com.example.productcrud.model.dto.reponse.ProductResponse;
import com.example.productcrud.model.dto.request.ProductPatchRequest;
import com.example.productcrud.model.dto.request.ProductRequest;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    PagedResponse<ProductResponse> getProductPage(Pageable pageable);

    PagedResponse<ProductResponse> searchProducts(String q, Pageable pageable);

    PagedResponse<ProductResponse> getProductPageByStatus(ProductStatus status, Pageable pageable);

    PagedResponse<ProductResponse> searchProductsByStatus(String q, ProductStatus status, Pageable pageable);

    Product addProduct(ProductRequest productRequest);

    ProductResponse addProductAndGetResponse(ProductRequest productRequest);

    Product getProductById(Integer id);

    ProductResponse getProductResponseById(Integer id);

    Product updateProduct(Integer id, ProductRequest productRequest);

    ProductResponse updateProductAndGetResponse(Integer id, ProductRequest productRequest);

    ProductResponse patchProduct(Integer id, ProductPatchRequest productPatchRequest);

    void deleteProduct(Integer id);
}
