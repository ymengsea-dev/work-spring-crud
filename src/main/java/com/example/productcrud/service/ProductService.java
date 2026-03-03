package com.example.productcrud.service;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.request.ProductRequest;

import java.util.List;

public interface ProductService {
    List<Product> getProduct();

    Product addProduct(ProductRequest productRequest);

    Product getProductById(Integer id);

    Product updateProduct(Integer id, ProductRequest productRequest);

    void deleteProduct(Integer id);
}
