package com.example.productcrud.service;

import com.example.productcrud.constraint.ProductStatus;
import com.example.productcrud.model.Product;
import com.example.productcrud.model.dto.reponse.PagedResponse;
import com.example.productcrud.model.dto.reponse.ProductResponse;
import com.example.productcrud.model.dto.request.CreateProductRequest;
import com.example.productcrud.model.dto.request.ProductPatchRequest;
import com.example.productcrud.model.dto.request.ProductRequest;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Product getAllProduct(Pageable pageable);

    Product addProduct(CreateProductRequest productRequest);

    Product getProductById(Integer id);

    Product updateProduct(Integer id, ProductRequest productRequest);

    ProductResponse patchProduct(Integer id, ProductPatchRequest productPatchRequest);

    void deleteProduct(Integer id);
}
