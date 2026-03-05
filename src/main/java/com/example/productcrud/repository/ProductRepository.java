package com.example.productcrud.repository;

import com.example.productcrud.constraint.ProductStatus;
import com.example.productcrud.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByProductCode(String code);

    Page<Product> findByProductCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String productCode,
                                                                                  String name,
                                                                                  Pageable pageable);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    Page<Product> findByStatusAndProductCodeContainingIgnoreCaseOrStatusAndNameContainingIgnoreCase(
            ProductStatus status1, String productCode,
            ProductStatus status2, String name,
            Pageable pageable);
}
