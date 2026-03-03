package com.example.productcrud.repository;

import com.example.productcrud.model.User;
import org.hibernate.boot.models.JpaAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends  JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

}
