package com.example.productcrud.service;


import com.example.productcrud.model.RefreshToken;
import com.example.productcrud.model.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Integer userId);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    void revokeByUser(User user);
    void deleteByUser(User user);
}
