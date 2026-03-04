package com.example.productcrud.service;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.reponse.PagedResponse;
import com.example.productcrud.model.reponse.ProductResponse;
import com.example.productcrud.model.request.ProductRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<Product> getProduct();

    PagedResponse<ProductResponse> getProductPage(Pageable pageable);

    Product addProduct(ProductRequest productRequest);

    ProductResponse addProductAndGetResponse(ProductRequest productRequest);

    Product getProductById(Integer id);

    ProductResponse getProductResponseById(Integer id);

    Product updateProduct(Integer id, ProductRequest productRequest);

    ProductResponse updateProductAndGetResponse(Integer id, ProductRequest productRequest);

    void deleteProduct(Integer id);
}
