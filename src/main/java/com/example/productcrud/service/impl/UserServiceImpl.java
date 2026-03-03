package com.example.productcrud.service.impl;

import com.example.productcrud.constraint.Role;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public User login(String email, String password) {
        // check user is present
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User doesn't exist"));

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid credential");
        }

        return user;
    }
}
