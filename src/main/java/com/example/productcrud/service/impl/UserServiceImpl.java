package com.example.productcrud.service.impl;

import com.example.productcrud.constraint.Role;
import com.example.productcrud.model.CustomUserDetail;
import com.example.productcrud.model.User;
import com.example.productcrud.model.dto.reponse.AuthResponse;
import com.example.productcrud.model.dto.reponse.UserResponse;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
        user.setRoles(new ArrayList<>(List.of(Role.PRODUCT_READ.toString())));
        userRepository.save(user);
    }

    @Override
    public AuthResponse login(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        Long expiredIn = (long) (60 * 60);

        AuthResponse response = AuthResponse.builder()
            .accessToken(token)
            .tokenType("Bearer")
            .expiresIn(expiredIn)
            .user(UserResponse.builder()
                    .userId(userDetails.getUser().getId())
                    .userName(userDetails.getUsername())
                    .roles(userDetails.getUser().getRoles())
                    .build())
            .build();

        return response;
    }
}
