package com.example.productcrud.service.impl;

import com.example.productcrud.constraint.ProductStatus;
import com.example.productcrud.exception.ProductAlreadyExistException;
import com.example.productcrud.exception.ProductHasActiveOrdersException;
import com.example.productcrud.exception.ProductNotFoundException;
import com.example.productcrud.model.Product;
import com.example.productcrud.model.dto.reponse.PagedResponse;
import com.example.productcrud.model.dto.reponse.ProductResponse;
import com.example.productcrud.model.dto.request.CreateProductRequest;
import com.example.productcrud.model.dto.request.ProductPatchRequest;
import com.example.productcrud.model.dto.request.ProductRequest;
import com.example.productcrud.repository.ProductRepository;
import com.example.productcrud.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "productCode", "name", "price", "status", "createdAt", "updatedAt"
    );

    private final ProductRepository productRepository;

    @Override
    public PagedResponse<ProductResponse> getProductPage(String query, ProductStatus status, int page, int size, String sortBy, String sortDir) {
        String keyword = (query == null || query.isBlank()) ? null : query.trim();
        String orderBy = sortBy != null && ALLOWED_SORT_FIELDS.contains(sortBy) ? sortBy : "id";
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, orderBy));

        Page<Product> productPage = productRepository.findProducts(keyword, status, pageable);
        List<ProductResponse> items = productPage.getContent().stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());

        return PagedResponse.<ProductResponse>builder()
                .items(items)
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalItems(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    public ProductResponse addProductAndGetResponse(CreateProductRequest productRequest) {
        productRepository.findByProductCode(productRequest.getCode())
                .ifPresent(p -> {
                    throw new ProductAlreadyExistException(
                            "Product code already exists.",
                            productRequest.getCode()
                    );
                });

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .productCode(productRequest.getCode())
                .price(productRequest.getPrice())
                .currency(productRequest.getCurrency())
                .status(ProductStatus.ACTIVE)
                .build();
        Product saved = productRepository.save(product);
        return toProductResponse(saved);
    }

    @Override
    public ProductResponse getProductResponseById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));
        return toProductResponse(product);
    }

    @Override
    public ProductResponse updateProductAndGetResponse(Integer id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Cannot update, product with id " + id + " does not exist."));

        product.setName(productRequest.getName());
        product.setProductCode(productRequest.getCode());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setCurrency(productRequest.getCurrency());
        product.setStatus(productRequest.getStatus());

        Product updated = productRepository.save(product);
        return toProductResponse(updated);
    }

    @Override
    public ProductResponse patchProduct(Integer id, ProductPatchRequest productPatchRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Cannot update, product with id " + id + " does not exist."));

        if (productPatchRequest.getPrice() != null) {
            product.setPrice(productPatchRequest.getPrice());
        }
        if (productPatchRequest.getStatus() != null) {
            product.setStatus(productPatchRequest.getStatus());
        }

        Product updated = productRepository.save(product);
        return toProductResponse(updated);
    }

    @Override
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Cannot delete, product with id " + id + " does not exist."));

        if (product.getStatus() == ProductStatus.ACTIVE) {
            throw new ProductHasActiveOrdersException("Product cannot be deleted because active orders exist.");
        }

        productRepository.deleteById(id);
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id("prd_" + product.getId())
                .code(product.getProductCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .status(product.getStatus())
                .createAt(product.getCreatedAt())
                .updateAt(product.getUpdatedAt())
                .build();
    }
}
