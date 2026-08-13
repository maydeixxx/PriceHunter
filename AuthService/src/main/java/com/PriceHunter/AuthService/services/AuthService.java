package com.PriceHunter.AuthService.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
public class AuthService {
    @Value("${access_token_lifetime}")
    private Duration accessTokenLifetime;

    private final SecretKey secretKey;

    public AuthService(@Value("${secret_key}") String signingKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(signingKey));
    }

    public String generateAccessToken(String email) {
        Date createdAt = new Date();
        Date expiresAt = new Date(createdAt.getTime() + accessTokenLifetime.toMillis());

        return Jwts.builder()
                .subject(email)
                .issuedAt(createdAt)
                .expiration(expiresAt)
                .signWith(secretKey)
                .compact();
    }
}
