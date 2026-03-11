package com.example.productcrud.service.impl;

import com.example.productcrud.constraint.AuthProvider;
import com.example.productcrud.constraint.ExceptionCode;
import com.example.productcrud.constraint.Role;
import com.example.productcrud.exception.BusinessException;
import com.example.productcrud.model.CustomUserDetail;
import com.example.productcrud.model.RefreshToken;
import com.example.productcrud.model.User;
import com.example.productcrud.model.dto.reponse.AuthResponse;
import com.example.productcrud.model.dto.reponse.RefreshTokenResponse;
import com.example.productcrud.model.dto.reponse.UserResponse;
import com.example.productcrud.model.dto.request.RefreshTokenRequest;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.service.RefreshTokenService;
import com.example.productcrud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.expiration}")
    String expiredIn;

    @Override
    public void register(String username, String email, String password) {
        // check use already exist
        if (userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("User is already exist");
        }
        // if not exist register user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setRoles(new ArrayList<>(List.of(Role.PRODUCT_READ.toString())));
        userRepository.save(user);
    }

    @Override
    public AuthResponse login(String email, String password) {
        if (!userRepository.findByEmail(email).isPresent()){
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    email,
                    password
                )
            );

            CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            refreshTokenService.deleteByUser(userDetails.getUser());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getId());

            AuthResponse response = AuthResponse.builder()
                    .accessToken(token)
                    .refreshToken(refreshToken.getToken())
                    .tokenType("Bearer")
                    .expiresIn(Long.valueOf(expiredIn))
                    .user(UserResponse.builder()
                            .userId(userDetails.getUser().getId())
                            .userName(userDetails.getUsername())
                            .roles(userDetails.getUser().getRoles())
                            .build())
                    .build();

            return response;
        } catch (BadCredentialsException exception) {
            throw  new BusinessException(ExceptionCode.INVALID_CREDENTIAL);
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        // find this refresh token
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken())
                .orElseThrow(()-> new BusinessException(ExceptionCode.REFRESH_TOKEN_NOT_FOUND));

        // check if valid
        refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();

        // give new access token
        String newAccessToken = jwtService.generateToken(new CustomUserDetail(user));

        RefreshTokenResponse response = RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .build();

        return response;
    }

    @Override
    public void logout(RefreshTokenRequest request) {
        refreshTokenService.findByToken(request.getRefreshToken())
                .ifPresent(token -> {refreshTokenService.revokeByUser(token.getUser());});
    }
}
