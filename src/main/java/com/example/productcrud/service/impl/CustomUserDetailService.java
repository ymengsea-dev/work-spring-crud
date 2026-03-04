package com.example.productcrud.service.impl;

import com.example.productcrud.exception.AccountInactiveException;
import com.example.productcrud.exception.AccountLockedException;
import com.example.productcrud.model.CustomUserDetail;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this email: " + email));

        if (!user.isActive()) {
            throw new AccountInactiveException("Account is inactive. Please contact administrator.");
        }

        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
            throw new AccountLockedException(
                    "Account is locked due to multiple failed login attempts.",
                    user.getLockedUntil()
            );
        }
        return new CustomUserDetail(user);
    }
}
