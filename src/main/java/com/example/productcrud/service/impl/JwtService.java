package com.example.productcrud.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // generate token method
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList()
        );

        Date issuedAt = new Date();
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hour

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // get signature key
    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //

    //get username from token
    public  String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    // check token is valid
    public boolean isTokenValid(String token, UserDetails userDetails){
        return extractClaims(token)
                .equals(userDetails.getUsername()) && !isExpiration(token);
    }

    // check token expiration
    public boolean isExpiration(String token){
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // extract payload
    public Claims extractClaims(String token){
        return  Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
