package com.example.productcrud.service.impl;

import com.example.productcrud.constraint.ProductStatus;
import com.example.productcrud.exception.ProductAlreadyExistException;
import com.example.productcrud.exception.ProductNotFoundException;
import com.example.productcrud.model.Product;
import com.example.productcrud.model.dto.reponse.PagedResponse;
import com.example.productcrud.model.dto.reponse.ProductResponse;
import com.example.productcrud.model.dto.request.ProductPatchRequest;
import com.example.productcrud.model.dto.request.ProductRequest;
import com.example.productcrud.repository.ProductRepository;
import com.example.productcrud.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // get all product
    @Override
    public PagedResponse<ProductResponse> getProductPage(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductResponse> items = page.getContent().stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
        return PagedResponse.<ProductResponse>builder()
                .items(items)
                .page(page.getNumber())
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public PagedResponse<ProductResponse> searchProducts(String q, Pageable pageable) {
        Page<Product> page = productRepository
                .findByProductCodeContainingIgnoreCaseOrNameContainingIgnoreCase(q, q, pageable);
        List<ProductResponse> items = page.getContent().stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
        return PagedResponse.<ProductResponse>builder()
                .items(items)
                .page(page.getNumber())
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public PagedResponse<ProductResponse> getProductPageByStatus(ProductStatus status, Pageable pageable) {
        Page<Product> page = productRepository.findByStatus(status, pageable);
        List<ProductResponse> items = page.getContent().stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
        return PagedResponse.<ProductResponse>builder()
                .items(items)
                .page(page.getNumber())
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public PagedResponse<ProductResponse> searchProductsByStatus(String q, ProductStatus status, Pageable pageable) {
        Page<Product> page = productRepository
                .findByStatusAndProductCodeContainingIgnoreCaseOrStatusAndNameContainingIgnoreCase(
                        status, q, status, q, pageable);
        List<ProductResponse> items = page.getContent().stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
        return PagedResponse.<ProductResponse>builder()
                .items(items)
                .page(page.getNumber())
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id("prd_" + product.getId())
                .code(product.getProductCode())
                .name(product.getName())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .status(product.getStatus())
                .createAt(product.getCreatedAt())
                .updateAt(product.getUpdatedAt())
                .build();
    }

    // add product
    @Override
    public Product addProduct(ProductRequest productRequest) {
        // check if the product with the product code exist
        productRepository.findByProductCode(productRequest.getCode())
                .ifPresent(p -> {
                    throw new ProductAlreadyExistException(
                            "Product code already exists.",
                            productRequest.getCode()
                    );
                });

        Product newProduct = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .productCode(productRequest.getCode())
                .price(productRequest.getPrice())
                .currency(productRequest.getCurrency())
                .status(productRequest.getStatus())
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public ProductResponse addProductAndGetResponse(ProductRequest productRequest) {
        return toProductResponse(addProduct(productRequest));
    }

    // get product by id
    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public ProductResponse getProductResponseById(Integer id) {
        return toProductResponse(getProductById(id));
    }

    // update product
    @Override
    public Product updateProduct(Integer id, ProductRequest productRequest) {
        // check if product exist
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Cannot update, product with id of " + id + "doesn't exist"));

        // set new value
        product.setName(productRequest.getName());
        product.setProductCode(productRequest.getCode());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setCurrency(productRequest.getCurrency());
        product.setStatus(productRequest.getStatus());

        // save new the update
        productRepository.save(product);

        return product;
    }

    @Override
    public ProductResponse updateProductAndGetResponse(Integer id, ProductRequest productRequest) {
        return toProductResponse(updateProduct(id, productRequest));
    }

    @Override
    public ProductResponse patchProduct(Integer id, ProductPatchRequest productPatchRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Cannot update, product with id of " + id + "doesn't exist"));

        if (productPatchRequest.getPrice() != null) {
            product.setPrice(productPatchRequest.getPrice());
        }
        if (productPatchRequest.getStatus() != null) {
            product.setStatus(productPatchRequest.getStatus());
        }

        productRepository.save(product);

        return toProductResponse(product);
    }

    // delete product
    @Override
    public void deleteProduct(Integer id) {
        productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Cannot delete, product with id of " + id + "doesn't exist"));

        productRepository.deleteById(id);
    }
}
