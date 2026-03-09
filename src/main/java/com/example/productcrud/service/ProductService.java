package com.example.productcrud.service;

import com.example.productcrud.constraint.ProductStatus;
import com.example.productcrud.model.dto.reponse.PagedResponse;
import com.example.productcrud.model.dto.reponse.ProductResponse;
import com.example.productcrud.model.dto.request.ProductPatchRequest;
import com.example.productcrud.model.dto.request.ProductRequest;

public interface ProductService {

    PagedResponse<ProductResponse> getProductPage(String query, ProductStatus status, int page, int size, String sortBy, String sortDir);

    ProductResponse addProductAndGetResponse(ProductRequest productRequest);

    ProductResponse getProductResponseById(Integer id);

    ProductResponse updateProductAndGetResponse(Integer id, ProductRequest productRequest);

    ProductResponse patchProduct(Integer id, ProductPatchRequest productPatchRequest);

    void deleteProduct(Integer id);
}
