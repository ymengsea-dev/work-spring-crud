package com.example.productcrud.repository;

import com.example.productcrud.constraint.ProductStatus;
import com.example.productcrud.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.productCode = :code")
    Optional<Product> findByProductCode(@Param("code") String code);

    @Query("SELECT p FROM Product p " +
            "WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:status IS NULL OR p.status = :status)")
    Page<Product> findProducts(@Param("keyword") String keyword, @Param("status") ProductStatus status, Pageable pageable);
}
