package com.example.productcrud.service.impl;

import com.example.productcrud.exception.ProductNotFoundException;
import com.example.productcrud.model.Product;
import com.example.productcrud.model.request.ProductRequest;
import com.example.productcrud.repository.ProductRepository;
import com.example.productcrud.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product addProduct(ProductRequest productRequest) {
        Product newProduct = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .code(productRequest.getCode())
                .price(productRequest.getPrice())
                .currency(productRequest.getCurrency())
                .status(productRequest.getStatus())
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product with id of" + id + "not found"));
    }

    @Override
    public Product updateProduct(Integer id, ProductRequest productRequest) {
        // check if product exist
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Cannot update, product with id of " + id + "doesn't exist"));

        // set new value
        product.setName(productRequest.getName());
        product.setCode(productRequest.getCode());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setCurrency(productRequest.getCurrency());
        product.setStatus(productRequest.getStatus());

        // save new the update
        productRepository.save(product);

        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Cannot delete, product with id of " + id + "doesn't exist"));

        productRepository.deleteById(id);
    }
}
