package com.example.productcrud.service;

import com.example.productcrud.model.User;

public interface UserService {
    void register(String username, String email, String password);
    User login(String email, String password);
}
