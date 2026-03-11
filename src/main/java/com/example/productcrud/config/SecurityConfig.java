package com.example.productcrud.config;

import com.example.productcrud.service.impl.CustomOAuth2UserService;
import com.example.productcrud.service.impl.CustomUserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthExpirationHandler jwtAuthExpirationHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler auth2SuccessHandler;
    private final OAuth2FailureHandler auth2FailureHandler;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement( sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthExpirationHandler)
                .accessDeniedHandler(customAccessDeniedHandler)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/v1/user/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/api/v1/files/**"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/products/**")
                    .hasAnyRole("PRODUCT_READ", "PRODUCT_WRITE")
                .requestMatchers("/api/v1/products/**")
                    .hasRole("PRODUCT_WRITE")
                .anyRequest().authenticated()
            ).oauth2Login(oauth -> oauth.
                    userInfoEndpoint(userInfo -> userInfo
                            .userService(customOAuth2UserService)
                    )
                    .successHandler(auth2SuccessHandler)
                    .failureHandler(auth2FailureHandler)
                )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // this method will load user from database
    // and check if logic credential match
    // with credential in database
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // use for authenticate the user
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

}
